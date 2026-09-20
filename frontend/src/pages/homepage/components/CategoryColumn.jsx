import styles from './CategoryColumn.module.css'
import DraggableTask from './DraggableTask'
import { useDroppable } from '@dnd-kit/core'

function CategoryColumn({ category, onEdit, onDelete, onAddTask, onEditTask, onDeleteTask, onToggleTask }) {

    const isUncategorized = category.title === 'Uncategorized'

    const tasks = [...(category.tasks ?? [])]
        .sort((a, b) => a.position - b.position)

    const {
        //betyder at det tilkoblede element er en dropzone
        setNodeRef,
        //boolean true betyder at en task er over en dropzone false = ikke over
        isOver,} = useDroppable({id: category.id,})

    return (
        <div className={`${styles.container} ${isOver ? styles.dragOver : ''}`} ref={setNodeRef}>
            <div className={styles.header}>
                <h3 className={styles.smallTitle}>{category.title}</h3>

                <div className={styles.actionsContainer}>
                    <button
                        className={styles.iconButton}
                        onClick={() => onAddTask(category)}
                        aria-label={`Add task to ${category.title}`}
                    >
                        +
                    </button>

                    {!isUncategorized && (
                        <>
                            <button
                                className={styles.iconButton}
                                onClick={() => onEdit(category)}
                                aria-label={`Edit ${category.title}`}
                            >
                                ✎
                            </button>

                            <button
                                className={styles.iconButton}
                                onClick={() => onDelete(category)}
                                aria-label={`Delete ${category.title}`}
                            >
                                ×
                            </button>
                        </>
                    )}
                </div>
            </div>

            {tasks.length === 0 && (
                <p className={styles.empty}>No tasks yet</p>
            )}

            <ul className={styles.taskList}>
                {tasks.map((task) => (
                    <DraggableTask
                        key={task.id}
                        task={task}
                        onEditTask={onEditTask}
                        onDeleteTask={onDeleteTask}
                        onToggleTask={onToggleTask}
                    />
                ))}
            </ul>
        </div>
    )
}

export default CategoryColumn