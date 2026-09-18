import styles from './CategoryColumn.module.css'

function CategoryColumn( { category, onEdit, onDelete, onAddTask }){

    const isUncategorized = category.title === 'Uncategorized'

    const tasks = [...(category.tasks ?? [])].sort((a, b) => a.position - b.position)

    return(
        <div className={styles.container}>
            <div className={styles.header}>
                <h3 className={styles.smallTitle}>{category.title}</h3>

                <div className={styles.actionsContainer}>
                    <button className={styles.iconButton} onClick={() => onAddTask(category)} aria-label={`Add task to ${category.title}`}>+</button>

                    {!isUncategorized && (
                        <>
                            <button className={styles.iconButton} onClick={() => onEdit(category)} aria-label={`Edit ${category.title}`}> ✎ </button>
                            <button className={styles.iconButton} onClick={() => onDelete(category)} aria-label={`Delete ${category.title}`}>×</button>
                        </>
                    )}
                </div>
            </div>

            {tasks.length === 0 && (
                <p className={styles.empty}>No tasks yet</p>
            )}

            <ul className={styles.taskList}>
                {tasks.map((task) => (
                    <li key={task.id} className={styles.task}>
                        <span className={styles.taskTitle}>{task.title}</span>

                        <span className={styles.taskMeta}>
                            <span className={styles.priority}>{task.priority}</span>
                            {task.price > 0 && (
                                <span className={styles.price}>{task.price} kr</span>
                            )}
                        </span>
                    </li>
                ))}
            </ul>
        </div>
    )

}

export default CategoryColumn
