import { useDraggable } from '@dnd-kit/core'
import styles from './CategoryColumn.module.css'

function DraggableTask({ task, onDeleteTask }) {
  const {
    //giver de nødvendige html-elementer
    attributes,
    //mouse-touch events
    listeners,
    //sættes på html-elementet der skal trækkes
    setNodeRef,
    //fortæller hvor elementet skal flyttes visuelt under drag
    transform,
  } = useDraggable({
    id: task.id,
  })
  const style = {
  transform: transform
    ? `translate3d(${transform.x}px, ${transform.y}px, 0)`
    : undefined,
}

  return (
    <li ref={setNodeRef} style={style} className={styles.task}{...listeners}{...attributes}>
      <span className={styles.taskTitle}>
        {task.title}
      </span>

      <span className={styles.taskMeta}>
        <span className={styles.priority}>
          {task.priority}
        </span>

        {task.price > 0 && (
          <span className={styles.price}>
            {task.price} kr
          </span>
        )}

        <button
          className={styles.iconButton}
          onClick={() => onDeleteTask(task)}
          aria-label={`Delete ${task.title}`}
        >
          ×
        </button>
      </span>
    </li>
  )
}
export default DraggableTask