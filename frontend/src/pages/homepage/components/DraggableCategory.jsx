import { useDraggable } from '@dnd-kit/core'
import CategoryColumn from './CategoryColumn'

function DraggableCategory({category, onEdit, onDelete, onAddTask, onEditTask, onDeleteTask}){

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
    id: category.id,
  })
  const style = {
  transform: transform
    ? `translate3d(${transform.x}px, ${transform.y}px, 0)`
    : undefined,
}    

return(

    <div ref={setNodeRef} style={style} >
<CategoryColumn 
//key=category.id
category={category} 
onEdit={onEdit}
onDelete={onDelete}
onAddTask={onAddTask}
onEditTask={onEditTask}
onDeleteTask={onDeleteTask}/>

<button {...listeners}{...attributes} >
    Move Cat
</button>
    </div>
)
}
export default DraggableCategory