import { rotuloStatus } from '../utils/format';

export default function StatusBadge({ status }) {
  return <span className={`badge badge-${(status || '').toLowerCase()}`}>{rotuloStatus(status)}</span>;
}
