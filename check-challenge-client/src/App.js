import React from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePublic from './react/pages/HomePublic';
import HomePrivate from './react/pages/HomePrivate';
import EvaluatorsPage from './react/pages/EvaluatorsPage';
import EvaluatePage from './react/pages/EvaluatePage';
import Signup from './react/pages/Signup';
import Login from './react/pages/Login';

function App() {

  return (
    <Router>
      <Routes>
        <Route exact path="/" element={ <HomePublic />} />
        <Route exact path="/home" element={<HomePrivate />} />
        <Route exact path='/register' element={<Signup />} />
        <Route exact path='/login' element={<Login />} />
        <Route exact path="/users" element={<EvaluatorsPage />} />
        <Route exact path="/evaluate/:challengeId" element={<EvaluatePage />} />
      </Routes>
    </Router>
  );
}

export default App;
