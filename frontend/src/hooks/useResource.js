import { useCallback, useEffect, useState } from 'react';
import api, { mensagemDeErro } from '../api/api';

/** Carrega uma lista (GET) e expõe { items, loading, erro, reload }. */
export default function useResource(path) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const r = await api.get(path);
      setItems(r.data);
      setErro('');
    } catch (e) {
      setErro(mensagemDeErro(e).mensagem);
    } finally {
      setLoading(false);
    }
  }, [path]);

  useEffect(() => {
    reload();
  }, [reload]);

  return { items, loading, erro, reload };
}
