import styles from './TaskList.module.css'

// ________________________________________________________

function formatDue(date) {
  return new Date(date).toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  })
}

// ________________________________________________________

function TaskList({ tasks, onToggle }) {
  return (
    <ul className={styles.list}>
      {tasks.map((task) => (
        <li key={task.id} className={task.done ? styles.itemDone : styles.item}>
          <label className={styles.label}>
            <input
              type="checkbox"
              checked={task.done}
              onChange={() => onToggle(task.id)}
            />
            <span className={styles.title}>{task.title}</span>
          </label>

          <span className={styles.category}>{task.category}</span>
          <span className={styles.due}>{formatDue(task.due)}</span>
          <span className={styles.badge + ' ' + styles[task.priority.toLowerCase()]}>
            {task.priority}
          </span>
        </li>
      ))}
    </ul>
  )
}

export default TaskList
