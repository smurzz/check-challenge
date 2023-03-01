import MainMenu from './components/MainMenu';
import React, { useState, useEffect } from "react";
import { useDispatch } from 'react-redux';
import { Button, Form, Alert } from 'react-bootstrap';
import { connect } from 'react-redux';

import * as authenticationActions from '../../redux/authentication/AuthenticationAction';
import * as evaluatorActions from '../../redux/user/EvaluatorAction';

import '../../layout/css/register-form.css'

const mapStateToPrors = state => {
    const { auth } = state
    return auth;
}

function Signup(props) {
    const dispatch = useDispatch();

    var formContent;
    var generalErrorMessage;
    var errorPassMessage;

    const [user, setUser] = useState(
        {
            firstName: "",
            lastName: "",
            position: "",
            email: "",
            password: ""
        });
    const [password, setPassword] = useState('');
    const [matchPass, setMatchPass] = useState('');
    const [disabledState, setDisabledState] = useState(false);
    const [validMatch, setValidMatch] = useState(false);
    const [error, setError] = useState(false);
    const [success, setSuccess] = useState(false);
    const clearEvalData = () => dispatch(evaluatorActions.clearDataAction());

    useEffect(() => {
        const match = password === matchPass;
        setValidMatch(match);
    }, [password, matchPass, validMatch]);

    useEffect(() => {
        const resSuccess = props.status === 201;
        setSuccess(resSuccess);
    }, [props.status, props.signupUserAction]);

    useEffect(() => {
        const resError = props.errorSignup !== null;
        setError(resError);
    }, [props.errorSignup]);

    useEffect(() => {
        const disabled = user.firstName === "" || 
            user.lastName === "" || 
            user.email === "" || 
            user.password === "" || 
            validMatch===false ? true : false;
        setDisabledState(disabled);
    }, [user, validMatch]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        props.signupUserAction(user);
        success ? setUser({
            firstName: "",
            lastName: "",
            position: "",
            email: "",
            password: ""
        }) : setUser(user);
        clearEvalData();
    }

    if (!validMatch) {
        errorPassMessage = (
            <Alert id="errorPassMessage" className="alarm text-center mt-3" key='danger' variant='danger'>
                Password must match the first password.
            </Alert>);
    }

    if (error === true) {
        const messageError = props.errorSignup + '';
        const formatedErrMessage = messageError.split(',').map(str => <p>{str}</p>);
        
        generalErrorMessage = (
            <Alert className="text-center mt-3" variant='danger'> {formatedErrMessage} </Alert>);
    }

    formContent = (<Form className="form mb-3 text-center">
        <h3 className="h3 mb-3 font-weight-normal">Register</h3>

        <Form.Control
            className="form-control"
            type="text"
            name='firstName'
            placeholder="First name*"
            onChange={async (e) => { setUser({ ...user, firstName: e.target.value }) }}
            required
            autoFocus />
        <Form.Control
            className="form-control"
            type="text"
            name='lastName'
            placeholder="Last name*"
            onChange={async (e) => { setUser({ ...user, lastName: e.target.value }) }}
            required />
        <Form.Control
            className="form-control"
            type="text"
            name='position'
            placeholder="Position"
            onChange={async (e) => { setUser({ ...user, position: e.target.value }) }} />
        <Form.Control
            className="form-control"
            type="email"
            name='email'
            placeholder="Email*"
            onChange={async (e) => { setUser({ ...user, email: e.target.value }) }}
            required />
        <Form.Control
            className="form-control"
            type="password"
            name='password'
            placeholder="Password*"
            onChange={async (e) => { setUser({ ...user, password: e.target.value }); setPassword(e.target.value) }}
            required />
        <Form.Control
            className="form-control"
            type="password"
            name='matchPass'
            placeholder="Password again*"
            onChange={(e) => { setMatchPass(e.target.value) }}
            aria-invalid={validMatch ? "false" : "true"}
            aria-describedby="errorPassMessage"
            required />
        <Button
            disabled={disabledState}
            type="submit"
            variant="dark"
            size="lg"
            onClick={handleSubmit}>

            Sign up
        </Button>

        {errorPassMessage}
        {generalErrorMessage}

        <p className="mt-5 mb-3 text-muted">&copy; 2023</p>
    </Form>);

    if (success === true) {
        formContent = (
            <Alert className="alarm text-center mt-3" key='success' variant='success'>
                <h1 className="cover-heading">Thank you for registration!</h1>
                <p className="lead">You have successfully registered! Now you can <Alert.Link href="/login">login</Alert.Link> and start 
                evaluating the challenges of applicantes for java developers. </p>
                <p >Or <Alert.Link href="/register">register</Alert.Link> another evaluater.</p>
            </Alert>);
    }

    return (
        <dev className="d-flex h-100 flex-column">
            < MainMenu />
            <dev className="form-container d-flex mb-auto mt-auto">
                {formContent}
            </dev>
        </dev>
    );
}

const mapDispatchToProps = dispatch => {
    return {
        signupUserAction: (user) => dispatch(authenticationActions.signupUser(user))
    }
}

const ConnectedSignupButton = connect(mapStateToPrors, mapDispatchToProps)(Signup)
export default ConnectedSignupButton;