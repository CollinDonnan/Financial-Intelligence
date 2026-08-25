import './App.css'
import { useEffect, useState } from 'react'
import Nav from './components/Nav'
import AbstractCard from './components/AbstractCard'
import TransactionPopup from './components/TransactionPopup'

function App() {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081'

  const [transactions, setTransactions] = useState(null)
  const [budget, setBudget] = useState({ amount: 0, remaining: 0 })
  const [error, setError] = useState(null)
  const [budgetError, setBudgetError] = useState(null)
  const [budgetInput, setBudgetInput] = useState('')
  const [savingBudget, setSavingBudget] = useState(false)
  const [showTransactionPopUp, setShowTransactionPopUp] = useState(false)
  const [editingTransaction, setEditingTransaction] = useState(null)
  const [deletingId, setDeletingId] = useState(null)

  useEffect(() => {
    fetch(`${baseUrl}/transactions`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`API request failed with status ${response.status}`)
        }
        return response.text()
      })
      .then((data) => setTransactions(JSON.parse(data)))
      .catch((requestError) => setError(requestError.message))
  }, [baseUrl])

  useEffect(() => {
    fetch(`${baseUrl}/budget/remaining`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Budget request failed with status ${response.status}`)
        }
        return response.json()
      })
      .then((data) => {
        setBudget({ amount: Number(data.amount ?? 0), remaining: Number(data.remaining ?? 0) })
        setBudgetInput(String(data.amount ?? 0))
      })
      .catch((requestError) => setBudgetError(requestError.message))
  }, [baseUrl])

  async function handleBudgetSave(event) {
    event.preventDefault()
    const amount = Number(budgetInput)
    if (!Number.isFinite(amount) || amount < 0) {
      setBudgetError('Enter a valid budget amount.')
      return
    }

    setSavingBudget(true)
    setBudgetError(null)
    try {
      const response = await fetch(`${baseUrl}/budget`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount }),
      })
      if (!response.ok) throw new Error(`Budget update failed with status ${response.status}`)
      await response.json()
      const refreshedResponse = await fetch(`${baseUrl}/budget/remaining`)
      if (!refreshedResponse.ok) throw new Error(`Budget refresh failed with status ${refreshedResponse.status}`)
      const data = await refreshedResponse.json()
      setBudget({ amount: Number(data.amount ?? amount), remaining: Number(data.remaining ?? amount) })
      setBudgetInput(String(data.amount ?? amount))
    } catch (requestError) {
      setBudgetError(requestError.message)
    } finally {
      setSavingBudget(false)
    }
  }

  const currencyFormatter = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })

  function handleSaved(transaction) {
    setTransactions((prev) => {
      const list = prev || []
      const index = list.findIndex((existing) => existing.id === transaction.id)
      if (index === -1) return [...list, transaction]
      const next = [...list]
      next[index] = transaction
      return next
    })
    setEditingTransaction(null)
  }

  async function handleDelete(id) {
    setDeletingId(id)
    try {
      const response = await fetch(`${baseUrl}/transaction/${id}`, { method: 'DELETE' })
      if (!response.ok) throw new Error(`Unable to delete transaction (${response.status})`)
      setTransactions((prev) => (prev || []).filter((transaction) => transaction.id !== id))
    } catch (requestError) {
      setError(requestError.message)
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <div className="app-layout">
      <Nav />
      <main className="main-content" id="overview">
        <header className="page-header">
          <div>
          </div>
        </header>
        <div className="card-grid">
          <AbstractCard title="Transactions">
            <div className="card-rule" />
            {error ? (
              <p className="transaction-value">API error: {error}</p>
            ) : transactions === null ? (
              <p className="transaction-value">Loading...</p>
            ) : transactions.length === 0 ? (
              <p className="transaction-value">No transactions yet.</p>
            ) : (
              <ul className="transaction-list">
                {transactions.map((transaction) => (
                  <li className="transaction-row" key={transaction.id}>
                    <span className="transaction-name">{transaction.name}</span>
                    <span className="transaction-amount">{currencyFormatter.format(transaction.amount)}</span>
                    <span className="transaction-actions">
                      <button className="text-button" type="button" onClick={() => setEditingTransaction(transaction)}>Edit</button>
                      <button
                        className="text-button transaction-delete"
                        type="button"
                        onClick={() => handleDelete(transaction.id)}
                        disabled={deletingId === transaction.id}
                      >
                        {deletingId === transaction.id ? 'Deleting...' : 'Delete'}
                      </button>
                    </span>
                  </li>
                ))}
              </ul>
            )}
            <button className="card-link" onClick={() => setShowTransactionPopUp(true)}>Add a transaction <span aria-hidden="true">&#8594;</span></button>
          </AbstractCard>

          <AbstractCard title="Monthly Budget">
            <div className="budget-card-body">
              {budgetError ? (
                <p className="transaction-value">Budget error: {budgetError}</p>
              ) : (
                <>
                  <div className="budget-top-value">{currencyFormatter.format(budget.amount)}</div>
                  <div className="budget-bottom-row">
                    <strong>{currencyFormatter.format(budget.remaining)}</strong>
                  </div>
                  <form className="budget-update-form" onSubmit={handleBudgetSave}>
                    <label htmlFor="monthly-budget">Set monthly budget</label>
                    <div className="budget-update-controls">
                      <input
                        id="monthly-budget"
                        type="number"
                        min="0"
                        step="0.01"
                        value={budgetInput}
                        onChange={(event) => setBudgetInput(event.target.value)}
                      />
                      <button type="submit" disabled={savingBudget}>
                        {savingBudget ? 'Saving...' : 'Save'}
                      </button>
                    </div>
                  </form>
                </>
              )}
            </div>
          </AbstractCard>
        </div>
      </main>
      {showTransactionPopUp && (
        <TransactionPopup
          baseUrl={baseUrl}
          onClose={() => setShowTransactionPopUp(false)}
          onSaved={handleSaved}
        />
      )}
      {editingTransaction && (
        <TransactionPopup
          baseUrl={baseUrl}
          transaction={editingTransaction}
          onClose={() => setEditingTransaction(null)}
          onSaved={handleSaved}
        />
      )}
    </div>
  )
}

export default App
