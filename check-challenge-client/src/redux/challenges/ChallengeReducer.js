import * as challengeActions from "./ChallengeAction";

const initialState = {
    challenges: [],
    loginPending: false,
    status: '',
    error: null
};

export default function challengeReducer(state = initialState, action){

    console.log('Bin in ChallengeReducer: ' + action.type);

    switch (action.type) {
        case challengeActions.REQUEST_READ_CHALLENGES:
            return {
                ...state,
                showLoginDialog: true,
                error: null
            }
        case challengeActions.SUCCESS_READ_CHALLENGES:
            return {
                ...state,
                pending: false,
                challenges: action.challenges,
                error: null
            }
        case challengeActions.FAIL_READ_CHALLENGES:
            return {
                ...state,
                pending: false,
                challenges: [],
                error: action.error
            }
        case challengeActions.REQUEST_EDIT_CHALLENGE:
            return {
                ...state,
                pending: true,
                error: null
            }
        case challengeActions.SUCCESS_EDIT_CHALLENGE:
            return {
                ...state,
                pending: false,
                status: action.status,
                error: null
            }
        case challengeActions.FAIL_EDIT_CHALLENGE:
            return {
                ...state,
                pending: false,
                error: action.error
            }
        default:
            return state;
    }

}