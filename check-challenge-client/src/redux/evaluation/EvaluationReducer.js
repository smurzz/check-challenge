import * as evaluationActions from "./EvaluationAction";

const initialState = {
    evaluation: [],
    pending: false,
    status: '',
    error: null,
};

export default function evaluationReducer(state = initialState, action) {

    console.log('Bin in EvaluationReducer: ' + action.type);

    switch (action.type) {
        case evaluationActions.REQUEST_CREATE_EVALUATION:
            return {
                ...state,
                pending: true,
                error: null
            }
        case evaluationActions.SUCCESS_CREATE_EVALUATION:
            return {
                ...state,
                pending: false,
                status: action.status,
                error: null
            }
        case evaluationActions.FAIL_CREATE_EVALUATION:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }

}