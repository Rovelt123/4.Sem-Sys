import styles from './CategoryColumn.module.css'
function CategoryColumn( { category, onEdit, onDelete }){

    const isUncategorized = category.title === 'Uncategorized'

    return(
        <div className={styles.container}>
            <div className={styles.header}>
                <h3 className={styles.smallTitle}>{category.title}</h3>
                
                {!isUncategorized && (
                    <div className={styles.actionsContainer}>
                        <button className={styles.iconButton} onClick={() => onEdit(category)} aria-label={`Edit ${category.title}`}> ✎ </button>
                        <button className={styles.iconButton} onClick={() => onDelete(category)} aria-label={`Delete ${category.title}`}>×</button>
                    </div>
                )}
            </div>
        </div>
    )

}

export default CategoryColumn