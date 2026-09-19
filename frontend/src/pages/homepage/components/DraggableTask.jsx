import { useDraggable } from '@dnd-kit/core'
import styles from './CategoryColumn.module.css'

function DraggableTask({ task, onEditTask, onDeleteTask }) {
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
      <span className={styles.taskHeader}>
        <span className={styles.taskTitle}>
          {task.title}
        </span>

        <span className={styles.actionsContainer}>
          <button
            className={styles.iconButton}
            onPointerDown={(e) => e.stopPropagation()}
            onClick={() => onEditTask(task)}
            aria-label={`Edit ${task.title}`}
          >
            ✎
          </button>

          <button
            className={styles.iconButton}
            onPointerDown={(e) => e.stopPropagation()}
            onClick={() => onDeleteTask(task)}
            aria-label={`Delete ${task.title}`}
          >
            ×
          </button>
        </span>
      </span>

      <span className={styles.taskMeta}>
        <span className={styles.metaRow}>
          <span className={styles.metaLabel}>Priority:</span>
          <span className={styles.metaValue}>{task.priority}</span>
        </span>

        {task.price > 0 && (
          <span className={styles.metaRow}>
            <span className={styles.metaLabel}>Price:</span>
            <span className={styles.metaValue}>{task.price.toLocaleString('en-US')} kr.</span>
          </span>
        )}
      </span>
    </li>
  )
}
export default DraggableTask
