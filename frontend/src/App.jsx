import { Routes, Route, Navigate } from "react-router";
import Landingpage from "./pages/landingpage/Landingpage";
import LoginPage from "./pages/login/LoginPage";
import RegisterPage from "./pages/register/RegisterPage";
import HomePage from "./pages/homepage/HomePage";
import PrivacyPolicyPage from "./pages/privacy/PrivacyPolicyPage";
import NotFoundPage from "./pages/notfound/NotFoundPage";
import CreateWeddingPage from "./pages/createwedding/CreateWeddingPage";
import ConfirmEmailPage from './pages/ConfirmEmailPage/ConfirmEmailPage';
import { getToken } from "./utils/storage";

// ________________________________________________________

function RequireToken({ children }) {
  return getToken() ? children : <Navigate to="/login" replace />;
}

// ________________________________________________________

function App() {
  return (
    <Routes>
      <Route path="/" element={getToken() ? <Navigate to="/homepage" replace /> : <Landingpage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/privacy" element={<PrivacyPolicyPage />} />
      <Route path="/confirm-email" element={<ConfirmEmailPage />}/>
      <Route
        path="/homepage"
        element={
          
            <HomePage />
          
        }
      />
      <Route
        path="/create"
        element={
          
            <CreateWeddingPage />
          
        }
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default App;
