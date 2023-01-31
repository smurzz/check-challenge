import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';

import * as Loader from 'react-loader-spinner';
import { usePromiseTracker } from "react-promise-tracker";
import axios from 'axios';
import thunk from 'redux-thunk';
import { applyMiddleware, compose, legacy_createStore as createStore } from 'redux';
import { Provider } from 'react-redux';
import rootReducer from './redux/RootReducer';

const initialState = {};
const middlewares = [thunk];

const store = createStore(rootReducer, initialState, compose(applyMiddleware(...middlewares)));

axios.defaults.baseURL = 'http://localhost:1453/';

const root = ReactDOM.createRoot(document.getElementById('root'));

const LoadingIndicator = props => {
  const { promiseInProgress } = usePromiseTracker();

  promiseInProgress &&
    <div
      style={{
        width: "100%",
        height: "100",
        display: "flex",
        justifyContent: "center",
        alignItems: "center"
      }}
    >
      <Loader.ThreeDots type="ThreeDots" color="#636363" height="100" width="100" />
    </div>

}

root.render(
  <Provider store={store}>
    < React.StrictMode >
      <App />
      <LoadingIndicator />
    </React.StrictMode >
  </Provider>
);

{/* <Provider store={ store }>
    <App />
  </Provider> */}

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
