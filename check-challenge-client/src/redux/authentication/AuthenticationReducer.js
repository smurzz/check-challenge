import * as authenticationActions from '../authentication/AuthenticationAction';

const initialState = {
    user: null,
    pending: false,
    errorLogin: null,
    errorSignup: null,
    status: ''
};

export default function authenticationReducer(state = initialState, action) {

    console.log('Bin in Reducer: ' + action.type);

    switch (action.type) {
        case authenticationActions.AUTHENTICATION_PENDING:
            return {
                ...state,
                pending: true,
                error: null
            }
        case authenticationActions.AUTHENTICATION_SUCCESS:
            return {
                ...state,
                pending: false,
                user: action.user,
                accessToken: action.accessToken,
                refreshToken: action.refreshToken
            }
        case authenticationActions.AUTHENTICATION_ERROR:
            return {
                ...state,
                pending: false,
                errorLogin: action.error.response.data
            }
        case authenticationActions.SING_UP_PENDING:
            return {
                ...state,
                pending: true,
                error: null
            }
        case authenticationActions.SING_UP_SUCCESS:
            return {
                ...state,
                pending: false,
                status: action.status,
            }
        case authenticationActions.SING_UP_ERROR:
            return {
                ...state,
                pending: false,
                errorSignup: action.error
            }
        case authenticationActions.LOG_OUT:
            return {
                ...state,
                pending: false,
                user: action.user,
                accessToken: action.accessToken
            }
        default:
            return state;
    }
};