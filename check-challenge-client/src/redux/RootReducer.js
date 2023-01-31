import { combineReducers } from 'redux';
import authenticationReducer from './authentication/AuthenticationReducer';
import evaluatorReducer from './user/EvaluatorReducer';

const rootReducer = combineReducers({
    auth: authenticationReducer, 
    eval: evaluatorReducer
});
export default rootReducer;