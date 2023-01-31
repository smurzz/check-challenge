import MainMenu from './components/MainMenu';
import React, { useState, useEffect } from "react";
import { Button, Form, Alert } from 'react-bootstrap';
import { connect } from 'react-redux';
import { useNavigate } from "react-router-dom";
import '../../layout/css/register-form.css'

import * as authenticationActions from '../../redux/authentication/AuthenticationAction';

const mapStateToPrors = state => {
    const { auth } = state
    return auth;
}

function Login(props) {

    var formContent;
    var generalErrorMessage;

    const [user, setUser] = useState(
        {
            email: "",
            password: ""
        });

    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        console.log(props.user);
        const resSuccess = props.user !== null;
        setSuccess(resSuccess);
        setUser({
            email: "",
            password: ""
        });
    }, [props.user, props.loginUserAction]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        props.loginUserAction(user);
    }

    if (props.errorLogin) {
        generalErrorMessage = (
            <Alert className="alarm text-center mt-3" variant='danger'>
                {props.errorLogin.message}
            </Alert>);
    }

    formContent = (<Form className="form mb-3 text-center">
        <h3 className="h3 mb-3 font-weight-normal">Login</h3>

        <Form.Control
            className="form-control"
            type="email"
            name='email'
            placeholder="Email"
            onChange={async (e) => { setUser({ ...user, email: e.target.value }); }}
            required />
        <Form.Control
            className="form-control"
            type="password"
            name='password'
            placeholder="Password"
            onChange={async (e) => { setUser({ ...user, password: e.target.value }) }}
            required />
        <Button
            disabled={ user.email === "" || user.password === ""}
            type="submit"
            variant="dark"
            size="lg"
            onClick={handleSubmit}>

            Login
        </Button>

        {generalErrorMessage}

        <p className="mt-5 mb-3 text-muted">&copy; 2023</p>
    </Form>);

if(success === true) {
    navigate('/home');
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
        loginUserAction: (user) => dispatch(authenticationActions.authenticateUser(user.email, user.password))
    }
}

const ConnectedLoginButton = connect(mapStateToPrors, mapDispatchToProps)(Login)
export default ConnectedLoginButton;
