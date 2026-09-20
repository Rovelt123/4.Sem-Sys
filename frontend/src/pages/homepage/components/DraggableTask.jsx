// useSortable er en funktion der gør et object til et element der kan flyttes rundt i en sorteret liste.
import { useSortable } from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities'
import styles from './CategoryColumn.module.css'

function DraggableTask({ task, onEditTask, onDeleteTask, onToggleTask }) {
  const {
    //Tilføjer ekstra information til HTML-elementet som det har brug for.
    attributes,
    //Tilføjer de events der skal bruges for at starte drag. (pointer down, pointer move, pointer up)
    listeners,
    //sætter referencen til et DOM-element (peger på hvad dnd-kit må måle og flytte)
    setNodeRef,
    //Transform indeholder beregningen til flytningen af et element. (animationen)
    transform,
    transition,
  } = useSortable({  
    id: task.id, //useSortable skal bruge taskId, så vi ved hvad active.id er.
  })
  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  }

  return (
    <li ref={setNodeRef} style={style} className={`${styles.task} ${task.completed ? styles.taskDone : ''} `} {...listeners} {...attributes}>
      <span className={styles.taskHeader}>
        <input
          className={styles.taskCheckbox}
          type="checkbox"
          checked={task.completed}
          onPointerDown={(e) => e.stopPropagation()}
          onChange={() => onToggleTask(task)}
          aria-label={`Mark ${task.title} as done`}
        />

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

        {task.link && (
          <span className={styles.metaRow}>
            <span className={styles.metaLabel}>Link:</span>
            <a
              className={styles.taskLink}
              href={task.link}
              target="_blank"
              rel="noreferrer"
              onPointerDown={(e) => e.stopPropagation()}
            >
              Open
            </a>
          </span>
        )}
      </span>
    </li>
  )
}
export default DraggableTask
