import axios from "axios";
import * as refreshTokenService from "../authentication/RefreshTokenService";

export const REQUEST_READ_CHALLENGES = 'REQUEST_READ_CHALLENGES';
export const SUCCESS_READ_CHALLENGES = 'SUCCESS_READ_CHALLENGES';
export const FAIL_READ_CHALLENGES = 'FAIL_READ_CHALLENGES';

export const REQUEST_EDIT_CHALLENGE = 'REQUEST_EDIT_CHALLENGE';
export const SUCCESS_EDIT_CHALLENGE = 'SUCCESS_EDIT_CHALLENGE';
export const FAIL_EDIT_CHALLENGE = 'FAIL_EDIT_CHALLENGE';

export function getChallengesPendingAction() {
    return {
        type: REQUEST_READ_CHALLENGES
    }
}

export function getChallengesSuccessAction(challenges) {
    return {
        type: SUCCESS_READ_CHALLENGES,
        challenges: challenges
    }
}

export function getChallengesErrorAction(error) {
    return {
        type: FAIL_READ_CHALLENGES,
        error: error
    }
}

export function editChallengePendingAction() {
    return {
        type: REQUEST_EDIT_CHALLENGE
    }
}

export function editChallengeSuccessAction(res) {
    return {
        type: SUCCESS_EDIT_CHALLENGE,
        status: res.status
    }
}

export function editChallengeErrorAction(error) {
    return {
        type: FAIL_EDIT_CHALLENGE,
        error: error
    }
}

export function getChallenges() {

    return async dispatch => {
        dispatch(getChallengesPendingAction());

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: { 'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken }
                };
                axios.get('/challenges', requestOptions)
                    .then(response => {
                        const challenges = response.data;
                        dispatch( getChallengesSuccessAction(challenges));
                    })
                    .catch(error => {
                        const errorMessage = error.message;
                        dispatch(getChallengesErrorAction(errorMessage));
                    })
            })
            .catch(err => dispatch(getChallengesErrorAction(err.message)));
    }
}

export function editChallenge(challengeID, challenge) {

    return async dispatch => {
        dispatch(editChallengeSuccessAction());
        var errorMessage;

        await refreshTokenService.refreshTokenIfExpired()
            .then(() => {
                const requestOptions = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + JSON.parse(localStorage.getItem('userSession')).accessToken
                    }
                };
                axios.put('/challenges/' + challengeID, challenge, requestOptions)
                    .then(response => {
                        dispatch(editChallengeSuccessAction(response));
                    })
                    .catch(error => {
                        errorMessage = error.response.data.message;
                        dispatch(editChallengeErrorAction(errorMessage));
                    })
            })
            .catch(error => {
                errorMessage = error.response.data.message;
                dispatch(editChallengeErrorAction(errorMessage));
            })
    }
}