import { useState } from 'react';
import Alert from '../components/Alert';
import Field from '../components/Field';
import SearchBox from '../components/SearchBox';
import StatusBadge from '../components/StatusBadge';
import useCrud from '../hooks/useCrud';
import useResource from '../hooks/useResource';
import { STATUS_SINISTRO, data, moeda, rotuloStatus } from '../utils/format';

const vazio = { descricao: '', dataOcorrencia: '', valorSolicitado: '', status: 'ABERTO', apoliceId: '' };

export default function Sinistros() {
  const { items, loading, erro: erroLista, reload } = useResource('/sinistros');
  const { items: apolices } = useResource('/apolices');
  const crud = useCrud({
    path: '/sinistros',
    vazio,
    paraPayload: (f) => ({
      ...f,
      valorSolicitado: f.valorSolicitado === '' ? null : Number(f.valorSolicitado),
      apoliceId: f.apoliceId === '' ? null : Number(f.apoliceId),
    }),
    deItem: (s) => ({
      descricao: s.descricao,
      dataOcorrencia: s.dataOcorrencia,
      valorSolicitado: String(s.valorSolicitado),
      status: s.status,
      apoliceId: String(s.apoliceId),
    }),
    aoMudar: reload,
  });
  const [busca, setBusca] = useState('');
  const f = crud.form;

  // Ao criar, só apólices ATIVAS; ao editar, mostra todas (o sinistro pode já estar numa cancelada).
  const opcoes = crud.editId ? apolices : apolices.filter((a) => a.status === 'ATIVA');

  const termo = busca.trim().toLowerCase();
  const lista = items.filter(
    (s) => !termo || `${s.descricao} ${s.apoliceNumero} ${s.clienteNome} ${s.status}`.toLowerCase().includes(termo)
  );

  return (
    <>
      <section className="card">
        <h2>{crud.editId ? `Editar sinistro #${crud.editId}` : 'Novo sinistro'}</h2>
        {opcoes.length === 0 && <Alert tipo="aviso">Não há apólices ativas. Cadastre uma apólice antes de abrir um sinistro.</Alert>}
        <form onSubmit={crud.enviar}>
          <Field label="Apólice" erro={crud.campos.apoliceId}>
            <select value={f.apoliceId} onChange={(e) => crud.setCampo('apoliceId', e.target.value)} required>
              <option value="">Selecione…</option>
              {opcoes.map((a) => (
                <option key={a.id} value={a.id}>{a.numero} — {a.clienteNome} ({rotuloStatus(a.status)})</option>
              ))}
            </select>
          </Field>
          <Field label="Data da ocorrência" erro={crud.campos.dataOcorrencia}>
            <input type="date" value={f.dataOcorrencia} onChange={(e) => crud.setCampo('dataOcorrencia', e.target.value)} required />
          </Field>
          <Field label="Valor solicitado (R$)" erro={crud.campos.valorSolicitado}>
            <input type="number" step="0.01" min="0" value={f.valorSolicitado} onChange={(e) => crud.setCampo('valorSolicitado', e.target.value)} required />
          </Field>
          <Field label="Status" erro={crud.campos.status}>
            <select value={f.status} onChange={(e) => crud.setCampo('status', e.target.value)}>
              {STATUS_SINISTRO.map((s) => <option key={s} value={s}>{rotuloStatus(s)}</option>)}
            </select>
          </Field>
          <Field label="Descrição" erro={crud.campos.descricao}>
            <input value={f.descricao} onChange={(e) => crud.setCampo('descricao', e.target.value)} required />
          </Field>
          <div className="acoes-form">
            <button className="primario" disabled={crud.salvando}>{crud.salvando ? 'Salvando…' : 'Salvar'}</button>
            {crud.editId && <button type="button" onClick={crud.limpar}>Cancelar</button>}
          </div>
        </form>
        <Alert>{crud.erro}</Alert>
        <Alert tipo="ok">{crud.ok}</Alert>
      </section>

      <section className="card">
        <div className="topo-lista">
          <h2>Sinistros ({lista.length})</h2>
          <SearchBox value={busca} onChange={setBusca} />
        </div>
        <Alert>{erroLista}</Alert>
        <div className="tabela-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Apólice</th><th>Cliente</th><th>Descrição</th><th>Ocorrência</th><th>Valor</th><th>Status</th><th>Ações</th></tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan="8" className="vazio">Carregando…</td></tr>}
              {!loading && lista.length === 0 && <tr><td colSpan="8" className="vazio">Nenhum sinistro encontrado.</td></tr>}
              {lista.map((s) => (
                <tr key={s.id}>
                  <td>{s.id}</td>
                  <td>{s.apoliceNumero}</td>
                  <td>{s.clienteNome}</td>
                  <td>{s.descricao}</td>
                  <td>{data(s.dataOcorrencia)}</td>
                  <td>{moeda(s.valorSolicitado)}</td>
                  <td><StatusBadge status={s.status} /></td>
                  <td className="acoes">
                    <button onClick={() => crud.editar(s)}>Editar</button>
                    <button className="perigo" onClick={() => crud.excluir(s.id)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </>
  );
}
