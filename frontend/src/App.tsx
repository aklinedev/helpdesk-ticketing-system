import "./App.css"
import { TicketsPage } from "./pages/TicketPage"

function App() {
    return (
        <main className="app">
            <header className="app-header">
                <h1>Help Desk</h1>
                <p>Ticket Management System</p>
            </header>

            <TicketsPage />
        </main>
    )
}

export default App