import axios from "axios";
import { trackPromise } from 'react-promise-tracker';
import * as refreshTokenService from "../authentication/RefreshTokenService";

export const REQUEST_READ_EVALUATORS = 'REQUEST_READ_EVALUATORS';
export const SUCCESS_READ_EVALUATORS = 'SUCCESS_READ_EVALUATORS';
export const FAIL_READ_EVALUATORS = 'FAIL_READ_EVALUATORS';

export const REQUEST_CREATE_EVALUATOR = 'REQUEST_CREATE_EVALUATOR';
export const SUCCESS_CREATE_EVALUATOR = 'SUCCESS_CREATE_EVALUATOR';
export const FAIL_CREATE_EVALUATOR = 'FAIL_CREATE_EVALUATOR';
export const CLEAR_DATA_EVALUATOR = 'CLEAR_DATA_EVALUATOR';

export const REQUEST_DELETE_EVALUATOR = 'REQUEST_DELETE_EVALUATOR';
export const SUCCESS_DELETE_EVALUATOR = 'SUCCESS_DELETE_EVALUATOR';
export const FAIL_DELETE_EVALUATOR = 'FAIL_DELETE_EVALUATOR';

export const REQUEST_EDIT_EVALUATOR = 'REQUEST_EDIT_EVALUATOR';
export const SUCCESS_EDIT_EVALUATOR = 'SUCCESS_EDIT_EVALUATOR';
export const FAIL_EDIT_EVALUATOR = 'FAIL_EDIT_EVALUATOR';

export function getEvaluatorsPendingAction() {
    return {
        type: REQUEST_READ_EVALUATORS
    }
}

export function getEvaluatorsSuccessAction(evaluators) {
    return {
        type: SUCCESS_READ_EVALUATORS,
        evaluators: evaluators
    }
}

export function getEvaluatorsErrorAction(error) {
    return {
        type: FAIL_READ_EVALUATORS,
        error: error
    }
}

export function createEvaluatorPendingAction() {
    return {
        type: REQUEST_CREATE_EVALUATOR
    }
}

export function createEvaluatorSuccessAction(res) {
    return {
        type: SUCCESS_CREATE_EVALUATOR,
        status: res.status
    }
}

export function createEvaluatorErrorAction(error) {
    return {
        type: FAIL_CREATE_EVALUATOR,
        error: error
    }
}

export function clearDataAction() {
    return {
        type: CLEAR_DATA_EVALUATOR
    }
}

export function deleteEvaluatorPendingAction() {
    return {
        type: REQUEST_DELETE_EVALUATOR
    }
}

export function deleteEvaluatorSuccessAction(res) {
    return {
        type: SUCCESS_EDIT_EVALUATOR,
        status: res.status
    }
}

export function deleteEvaluatorErrorAction(error) {
    return {
        type: FAIL_EDIT_EVALUATOR,
        status: error.status
    }
}

export function editEvaluatorPendingAction() {
    return {
        type: REQUEST_EDIT_EVALUATOR
    }
}

export function editEvaluatorSuccessAction(res) {
    return {
        type: SUCCESS_EDIT_EVALUATOR,
        status: res.status
    }
}

export function editEvaluatorErrorAction(error) {
    return {
        type: FAIL_EDIT_EVALUATOR,
        error: error
    }
}

export function getEvaluators() {

    return async dispatch => {
        dispatch(getEvaluatorsPendingAction());

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: { 'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken }
                };
                trackPromise(axios.get('/users', requestOptions))
                    .then(response => {
                        const evaluaters = response.data;
                        const action = getEvaluatorsSuccessAction(evaluaters);
                        dispatch(action);
                    })
                    .catch(error => {
                        const errorMessage = error.message;
                        dispatch(getEvaluatorsErrorAction(errorMessage));
                    })
            })
            .catch(err => dispatch(getEvaluatorsErrorAction(err.message)));
    }
}

export function createEvaluator(evaluators) {

    return async dispatch => {
        dispatch(getEvaluatorsPendingAction());
        var errorMessage;

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken
                    }
                };
                axios.post('/users', evaluators, requestOptions)
                    .then(response => {
                        dispatch(createEvaluatorSuccessAction(response));
                    })
                    .catch(error => {
                        errorMessage = error.response.data;
                        dispatch(createEvaluatorErrorAction(errorMessage));
                    })
            })
            .catch(error => {
                errorMessage = error.response.data.message;
                dispatch(getEvaluatorsErrorAction(error));
            })
    }
}

export function deleteEvaluator(evaluatorID) {
    
    return async dispatch => {
        dispatch(deleteEvaluatorPendingAction());
        var errorMessage;

        await refreshTokenService.refreshTokenIfExpired()
        .then(() => {
            const requestOptions = {
                headers: {
                    'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken
                }
            };
            axios.delete('/users/' + evaluatorID, requestOptions)
                .then(response => {
                    const action = deleteEvaluatorSuccessAction(response);
                    dispatch(action);
                })
                .catch(error => {
                    errorMessage = error.message;
                    dispatch(deleteEvaluatorErrorAction(errorMessage));
                })
        })
        .catch(error => {
            errorMessage = error.response.data.message;
            dispatch(deleteEvaluatorErrorAction(errorMessage));
        })

    }
}

export function editEvaluator(evaluatorID, evaluator) {

    return async dispatch => {
        dispatch(editEvaluatorPendingAction());
        var errorMessage;

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken
                    }
                };
                axios.put('/users/' + evaluatorID, evaluator, requestOptions)
                    .then(response => {
                        const action = editEvaluatorSuccessAction(response);
                        dispatch(action);
                    })
                    .catch(error => {
                        errorMessage = error.response.data;
                        dispatch(editEvaluatorErrorAction(errorMessage));
                    })
            })
            .catch(error => {
                errorMessage = error.response.data.message;
                dispatch(editEvaluatorErrorAction(errorMessage));
            })
    }
}