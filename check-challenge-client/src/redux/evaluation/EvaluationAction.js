import axios from "axios";
import * as refreshTokenService from "../authentication/RefreshTokenService";

export const REQUEST_CREATE_EVALUATION = 'REQUEST_CREATE_EVALUATION';
export const SUCCESS_CREATE_EVALUATION = 'SUCCESS_CREATE_EVALUATION';
export const FAIL_CREATE_EVALUATION = 'FAIL_CREATE_EVALUATION';

export function createEvaluationPendingAction() {
    return {
        type: REQUEST_CREATE_EVALUATION
    }
}

export function createEvaluationSuccessAction(res) {
    return {
        type: SUCCESS_CREATE_EVALUATION,
        status: res.status
    }
}

export function createEvaluationErrorAction(error) {
    return {
        type: FAIL_CREATE_EVALUATION,
        error: error
    }
}

export function createEvaluatoion(evaluation) {

    return async dispatch => {
        dispatch(createEvaluationPendingAction());
        var errorMessage;

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken
                    }
                };
                axios.post('/evaluations', evaluation, requestOptions)
                    .then(response => {
                        dispatch(createEvaluationSuccessAction(response));
                    })
                    .catch(error => {
                        errorMessage = error.response.data.message;
                        dispatch(createEvaluationErrorAction(errorMessage));
                    })
            })
            .catch(error => {
                errorMessage = error.response.data.message;
                dispatch(createEvaluationErrorAction(error));
            })
    }
}