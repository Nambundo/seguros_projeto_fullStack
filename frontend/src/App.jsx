import { useState } from 'react';
import Apolices from './pages/Apolices';
import Clientes from './pages/Clientes';
import Dashboard from './pages/Dashboard';
import Sinistros from './pages/Sinistros';

const ABAS = [
  { id: 'dashboard', titulo: 'Painel', Pagina: Dashboard },
  { id: 'clientes', titulo: 'Clientes', Pagina: Clientes },
  { id: 'apolices', titulo: 'Apólices', Pagina: Apolices },
  { id: 'sinistros', titulo: 'Sinistros', Pagina: Sinistros },
];

export default function App() {
  const [aba, setAba] = useState('dashboard');
  const { Pagina } = ABAS.find((a) => a.id === aba);

  return (
    <main>
      <header>
        <h1>InsuranceHub</h1>
        <p>Java • Spring Boot • React • SQL Server</p>
      </header>

      <nav className="abas">
        {ABAS.map((a) => (
          <button key={a.id} className={a.id === aba ? 'ativa' : ''} onClick={() => setAba(a.id)}>
            {a.titulo}
          </button>
        ))}
      </nav>

      {/* key força remontar a página ao trocar de aba => dados sempre atualizados */}
      <Pagina key={aba} />
    </main>
  );
}
