import { Routes, Route } from "react-router";
import Landingpage from "./pages/landingpage/Landingpage"; 
import './App.css'

function App() {
  

  return (
    <Routes>
      <Route path="/" element={<Landingpage />} />
    </Routes>
  )
}

export default App
