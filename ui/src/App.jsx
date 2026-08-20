import './App.css'
import { useEffect, useState } from 'react'

function App() {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081'

  const [charge, setCharge] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch(`${baseUrl}/charge`)
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
    <div className="App">
      <h1>Hello World</h1>
      <p>{error ? `API error: ${error}` : charge || 'Connecting to API...'}</p>
    </div>
  )
}

export default App
