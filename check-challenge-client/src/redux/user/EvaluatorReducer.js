import * as evaluatorActions from "./EvaluatorAction";

const initialState = {
    evaluators: [],
    loginPending: false,
    status: '',
    errorGetUsers: null,
    errorCreateUser: null
};

export default function userReducer(state = initialState, action) {

    console.log('Bin in EvaluatorReducer: ' + action.type);

    switch (action.type) {
        case evaluatorActions.REQUEST_READ_EVALUATORS:
            return {
                ...state,
                showLoginDialog: true,
                errorGetUsers: null
            }
        case evaluatorActions.SUCCESS_READ_EVALUATORS:
            return {
                ...state,
                pending: false,
                evaluators: action.evaluators,
                errorGetUsers: null
            }
        case evaluatorActions.FAIL_READ_EVALUATORS:
            return {
                ...state,
                pending: false,
                evaluators: [],
                errorGetUsers: action.error
            }
        case evaluatorActions.REQUEST_CREATE_EVALUATOR:
            return {
                ...state,
                showLoginDialog: true,
                errorCreateUser: null
            }
        case evaluatorActions.SUCCESS_CREATE_EVALUATOR:
            return {
                ...state,
                pending: false,
                status: action.status,
                errorCreateUser: null
            }
        case evaluatorActions.FAIL_CREATE_EVALUATOR:
            return {
                ...state,
                pending: false,
                errorCreateUser: action.error
            }
        case evaluatorActions.CLEAR_DATA_CREATE_EVALUATOR:
            return{
                ...state,
                pending: false,
                status: '',
                errorCreateUser: null
            }
        case evaluatorActions.REQUEST_DELETE_EVALUATOR:
            return {
                ...state,
                showLoginDialog: true,
                error: null
            }
        case evaluatorActions.SUCCESS_DELETE_EVALUATOR:
            return {
                ...state,
                pending: false,
                status: action.status,
                error: null
            }
        case evaluatorActions.FAIL_DELETE_EVALUATOR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        case evaluatorActions.REQUEST_EDIT_EVALUATOR:
            return {
                ...state,
                showLoginDialog: true,
                error: null
            }
        case evaluatorActions.SUCCESS_EDIT_EVALUATOR:
            return {
                ...state,
                pending: false,
                status: action.status,
                error: null
            }
        case evaluatorActions.FAIL_EDIT_EVALUATOR:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }
};