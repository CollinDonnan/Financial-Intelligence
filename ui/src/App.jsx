import './App.css'
import { useEffect, useState } from 'react'
import Nav from './components/Nav'
import AbstractCard from './components/AbstractCard'
import TransactionPopup from './components/TransactionPopup'

function App() {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081'

  const [charge, setCharge] = useState(null)
  const [error, setError] = useState(null)
  const [showTransactionPopUp, setShowTransactionPopUp] = useState(false)

  useEffect(() => {
    fetch(`${baseUrl}/transactions`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`API request failed with status ${response.status}`)
        }
        return response.text()
      })
      .then((data) => setCharge(data))
      .catch((requestError) => setError(requestError.message))
  }, [baseUrl])


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
            <p className="transaction-value">{error ? `API error: ${error}` : charge || 'Loading...'}</p>
            <button className="card-link" onClick={() => setShowTransactionPopUp(true)}>Add a transaction <span aria-hidden="true">&#8594;</span></button>
          </AbstractCard>
          <section className="dashboard-card blank-card" aria-label="Empty dashboard card" />
        </div>
      </main>
      {showTransactionPopUp && (
        <TransactionPopup
          baseUrl={baseUrl}
          onClose={() => setShowTransactionPopUp(false)}
          onSaved={(transaction) => setCharge(JSON.stringify(transaction))}
        />
      )}
    </div>
  )
}

export default App
