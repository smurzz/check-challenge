import { combineReducers } from 'redux';
import authenticationReducer from './authentication/AuthenticationReducer';
import evaluatorReducer from './user/EvaluatorReducer';
import challengeReducer from './challenges/ChallengeReducer';
import evaluationReducer from './evaluation/EvaluationReducer';

const rootReducer = combineReducers({
    auth: authenticationReducer, 
    evaluator: evaluatorReducer,
    challenge: challengeReducer,
    evaluation: evaluationReducer
});
export default rootReducer;