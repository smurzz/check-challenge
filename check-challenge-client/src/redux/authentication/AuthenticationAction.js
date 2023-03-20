import jwt_decode from "jwt-decode";
import axios from "axios";

export const AUTHENTICATION_PENDING = 'AUTHENTICATION_PENDING';
export const AUTHENTICATION_SUCCESS = 'AUTHENTICATION_SUCCESS';
export const AUTHENTICATION_ERROR = 'AUTHENTICATION_ERROR';

export const SING_UP_PENDING = 'SING_UP_PENDING';
export const SING_UP_SUCCESS = 'SING_UP_SUCCESS';
export const SING_UP_ERROR = 'SING_UP_ERROR';

export const LOG_OUT = 'LOG_OUT';

export function getAuthenticateUserPendingAction() {
    return {
        type: AUTHENTICATION_PENDING
    }
}

export function getAuthenticationSuccessAction(userSession) {
    return {
        type: AUTHENTICATION_SUCCESS,
        user: userSession.user,
        accessToken: userSession.accessToken,
        refreshToken: userSession.refreshToken
    }
}

export function getAuthenticationErrorAction(error) {
    return {
        type: AUTHENTICATION_ERROR,
        error: error
    }
}

export function getSignupePendingAction() {
    return {
        type: SING_UP_PENDING
    }
}

export function getSignupSuccessAction(res) {
    return {
        type: SING_UP_SUCCESS,
        status: res.status
    }
}

export function getSignupErrorAction(error) {
    return {
        type: SING_UP_ERROR,
        error: error
    }
}

export function getLogOutAction(userSession) {
    return {
        type: LOG_OUT,
        user: userSession.user,
        accessToken: userSession.accessToken
    }
}

export function authenticateUser(email, password) {

    return dispatch => {

        dispatch(getAuthenticateUserPendingAction());
        login(email, password)
            .then(
                userSession => {
                    dispatch(getAuthenticationSuccessAction(userSession));
                },
                error => {
                    dispatch(getAuthenticationErrorAction(error));
                }
            )
            .catch(error => {
                dispatch(getAuthenticationErrorAction(error));
            })
    }
}

export function logoutUser() {
    console.log('Logout');
    const userSession = logout();

    return dispatch => {
        const action = getLogOutAction(userSession);
        dispatch(action);
    }
}

export function signupUser(user) {
    return dispatch => {
        dispatch(getSignupePendingAction());
        const requestOptions = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        axios.post('/auth/register', user, requestOptions)
            .then(response => {
                dispatch(getSignupSuccessAction(response))
            })
            .catch(error => {
                const errorMessage = error.response.data;
                dispatch(getSignupErrorAction(errorMessage));
            })

    }
}

function login(email, password) {

    return axios.post('/auth/login', {
        email: email,
        password: password
    }).then(response => {
        var userData = jwt_decode(response.data.access_token);
        var userSession = {
            user: userData,
            accessToken: response.data.access_token,
            refreshToken: response.data.refresh_token
        }
        localStorage.setItem('userSession', JSON.stringify(userSession));
        return userSession;
    })
        .catch(error => {
            return Promise.reject(error);
        });
}

function logout() {
    console.error('Logout user');
    localStorage.removeItem('userSession');
    var userSession = {
        user: null,
        accessToken: null,
        refreshToken: null
    }
    return userSession;
}