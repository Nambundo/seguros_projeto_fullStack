import { useState } from 'react';
import Alert from '../components/Alert';
import Field from '../components/Field';
import SearchBox from '../components/SearchBox';
import StatusBadge from '../components/StatusBadge';
import useCrud from '../hooks/useCrud';
import useResource from '../hooks/useResource';
import { STATUS_APOLICE, TIPOS_SEGURO, data, moeda, rotuloStatus } from '../utils/format';

const vazio = {
  numero: '',
  tipoSeguro: TIPOS_SEGURO[0],
  valorPremio: '',
  valorCobertura: '',
  dataInicio: '',
  dataFim: '',
  status: 'ATIVA',
  clienteId: '',
};

export default function Apolices() {
  const { items, loading, erro: erroLista, reload } = useResource('/apolices');
  const { items: clientes } = useResource('/clientes');
  const crud = useCrud({
    path: '/apolices',
    vazio,
    paraPayload: (f) => ({
      ...f,
      valorPremio: f.valorPremio === '' ? null : Number(f.valorPremio),
      valorCobertura: f.valorCobertura === '' ? null : Number(f.valorCobertura),
      clienteId: f.clienteId === '' ? null : Number(f.clienteId),
    }),
    deItem: (a) => ({
      numero: a.numero,
      tipoSeguro: a.tipoSeguro,
      valorPremio: String(a.valorPremio),
      valorCobertura: String(a.valorCobertura),
      dataInicio: a.dataInicio,
      dataFim: a.dataFim,
      status: a.status,
      clienteId: String(a.clienteId),
    }),
    aoMudar: reload,
  });
  const [busca, setBusca] = useState('');
  const f = crud.form;

  const termo = busca.trim().toLowerCase();
  const lista = items.filter(
    (a) => !termo || `${a.numero} ${a.tipoSeguro} ${a.clienteNome} ${a.status}`.toLowerCase().includes(termo)
  );

  return (
    <>
      <section className="card">
        <h2>{crud.editId ? `Editar apólice #${crud.editId}` : 'Nova apólice'}</h2>
        {clientes.length === 0 && <Alert tipo="aviso">Cadastre ao menos um cliente antes de criar uma apólice.</Alert>}
        <form onSubmit={crud.enviar}>
          <Field label="Número" erro={crud.campos.numero}>
            <input value={f.numero} placeholder="AP-001" onChange={(e) => crud.setCampo('numero', e.target.value)} required />
          </Field>
          <Field label="Tipo de seguro" erro={crud.campos.tipoSeguro}>
            <select value={f.tipoSeguro} onChange={(e) => crud.setCampo('tipoSeguro', e.target.value)}>
              {TIPOS_SEGURO.map((t) => <option key={t}>{t}</option>)}
            </select>
          </Field>
          <Field label="Cliente" erro={crud.campos.clienteId}>
            <select value={f.clienteId} onChange={(e) => crud.setCampo('clienteId', e.target.value)} required>
              <option value="">Selecione…</option>
              {clientes.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
            </select>
          </Field>
          <Field label="Prêmio (R$)" erro={crud.campos.valorPremio}>
            <input type="number" step="0.01" min="0" value={f.valorPremio} onChange={(e) => crud.setCampo('valorPremio', e.target.value)} required />
          </Field>
          <Field label="Cobertura (R$)" erro={crud.campos.valorCobertura}>
            <input type="number" step="0.01" min="0" value={f.valorCobertura} onChange={(e) => crud.setCampo('valorCobertura', e.target.value)} required />
          </Field>
          <Field label="Início" erro={crud.campos.dataInicio}>
            <input type="date" value={f.dataInicio} onChange={(e) => crud.setCampo('dataInicio', e.target.value)} required />
          </Field>
          <Field label="Fim" erro={crud.campos.dataFim}>
            <input type="date" value={f.dataFim} onChange={(e) => crud.setCampo('dataFim', e.target.value)} required />
          </Field>
          <Field label="Status" erro={crud.campos.status}>
            <select value={f.status} onChange={(e) => crud.setCampo('status', e.target.value)}>
              {STATUS_APOLICE.map((s) => <option key={s} value={s}>{rotuloStatus(s)}</option>)}
            </select>
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
          <h2>Apólices ({lista.length})</h2>
          <SearchBox value={busca} onChange={setBusca} />
        </div>
        <Alert>{erroLista}</Alert>
        <div className="tabela-wrap">
          <table>
            <thead>
              <tr><th>Número</th><th>Cliente</th><th>Tipo</th><th>Prêmio</th><th>Cobertura</th><th>Vigência</th><th>Status</th><th>Ações</th></tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan="8" className="vazio">Carregando…</td></tr>}
              {!loading && lista.length === 0 && <tr><td colSpan="8" className="vazio">Nenhuma apólice encontrada.</td></tr>}
              {lista.map((a) => (
                <tr key={a.id}>
                  <td>{a.numero}</td>
                  <td>{a.clienteNome}</td>
                  <td>{a.tipoSeguro}</td>
                  <td>{moeda(a.valorPremio)}</td>
                  <td>{moeda(a.valorCobertura)}</td>
                  <td>{data(a.dataInicio)} – {data(a.dataFim)}</td>
                  <td><StatusBadge status={a.status} /></td>
                  <td className="acoes">
                    <button onClick={() => crud.editar(a)}>Editar</button>
                    <button className="perigo" onClick={() => crud.excluir(a.id)}>Excluir</button>
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
