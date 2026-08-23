
import { useEffect, useState } from 'react'

type Transaction = { name: string; amount: number }

type TransactionPopupProps = {
  baseUrl: string
  onClose: () => void
  onSaved: (transaction: Transaction) => void
}

export default function TransactionPopup({ baseUrl, onClose, onSaved }: TransactionPopupProps) {
  const [name, setName] = useState('')
  const [amount, setAmount] = useState('')
  const [error, setError] = useState('')
  const [isSaving, setIsSaving] = useState(false)

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose()
    }
    document.addEventListener('keydown', handleKeyDown)
    return () => document.removeEventListener('keydown', handleKeyDown)
  }, [onClose])

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const parsedAmount = Number(amount)
    if (!name.trim() || !Number.isFinite(parsedAmount) || parsedAmount <= 0) {
      setError('Enter a name and a positive amount.')
      return
    }

    setError('')
    setIsSaving(true)
    try {
      const response = await fetch(`${baseUrl}/transactions`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name.trim(), amount: parsedAmount }),
      })
      if (!response.ok) throw new Error(`Unable to save transaction (${response.status})`)
      onSaved(await response.json())
      onClose()
    } catch (requestError) {
      setError(requestError instanceof Error ? requestError.message : 'Unable to save transaction.')
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <div className="modal-backdrop" onMouseDown={(event) => event.target === event.currentTarget && onClose()}>
      <section className="transaction-modal" role="dialog" aria-modal="true" aria-labelledby="transaction-title">
        <div className="modal-heading">
          <div><p className="eyebrow">New entry</p><h2 id="transaction-title">Add transaction</h2></div>
          <button className="modal-close" type="button" onClick={onClose} aria-label="Close modal">&times;</button>
        </div>
        <form onSubmit={handleSubmit}>
          <label>Description<input autoFocus value={name} onChange={(event) => setName(event.target.value)} placeholder="e.g. Monthly rent" /></label>
          <label>Amount<div className="amount-input"><span>$</span><input type="number" min="0.01" step="0.01" value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="0.00" /></div></label>
          {error && <p className="form-error" role="alert">{error}</p>}
          <div className="modal-actions"><button className="text-button" type="button" onClick={onClose}>Cancel</button><button className="add-button" type="submit" disabled={isSaving}>{isSaving ? 'Saving...' : 'Save transaction'}</button></div>
        </form>
      </section>
    </div>
  )
}