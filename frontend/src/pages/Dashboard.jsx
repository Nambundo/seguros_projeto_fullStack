import { useEffect, useState } from 'react';
import api, { mensagemDeErro } from '../api/api';
import Alert from '../components/Alert';
import { moeda } from '../utils/format';

export default function Dashboard() {
  const [dados, setDados] = useState(null);
  const [erro, setErro] = useState('');

  useEffect(() => {
    api
      .get('/dashboard')
      .then((r) => setDados(r.data))
      .catch((e) => setErro(mensagemDeErro(e).mensagem));
  }, []);

  if (erro) return <Alert>{erro}</Alert>;
  if (!dados) return <p className="vazio">Carregando…</p>;

  const cards = [
    ['Clientes', dados.totalClientes],
    ['Apólices ativas', dados.apolicesAtivas],
    ['Sinistros em andamento', dados.sinistrosEmAndamento],
    ['Prêmios (apólices ativas)', moeda(dados.premiosAtivos)],
    ['Cobertura total ativa', moeda(dados.coberturaAtiva)],
    ['Valor em sinistros em andamento', moeda(dados.valorEmSinistrosEmAndamento)],
  ];

  return (
    <div className="grid-cards">
      {cards.map(([titulo, valor]) => (
        <div className="card kpi" key={titulo}>
          <span>{titulo}</span>
          <strong>{valor}</strong>
        </div>
      ))}
    </div>
  );
}
