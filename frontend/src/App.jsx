import { Routes, Route, Navigate } from "react-router";
import Landingpage from "./pages/landingpage/Landingpage";
import LoginPage from "./pages/login/LoginPage";
import RegisterPage from "./pages/register/RegisterPage";
import HomePage from "./pages/homepage/HomePage";
import PrivacyPolicyPage from "./pages/privacy/PrivacyPolicyPage";
import NotFoundPage from "./pages/notfound/NotFoundPage";
import { getToken } from "./utils/storage";
import './App.css';

// ________________________________________________________

function RequireToken({ children }) {
  return getToken() ? children : <Navigate to="/login" replace />;
}

// ________________________________________________________

function App() {
  return (
    <Routes>
      <Route path="/" element={<Landingpage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/privacy" element={<PrivacyPolicyPage />} />
      <Route
        path="/homepage"
        element={
          <RequireToken>
            <HomePage />
          </RequireToken>
        }
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default App;
