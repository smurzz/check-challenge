import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { TfiPencilAlt } from "react-icons/tfi";

import * as evaluatorActions from '../../../redux/user/EvaluatorAction';

function NewEvaluator(props) {      /* { refresh, evaluator } */

    const userRoles = ["ADMIN", "USER"];
    var buttonUpdateOrCreate;
    var formContent;
    var errorMessage;
    var tmpRoles = new Set();
    const ev = props.evaluator;

    const evalData = useSelector(state => state.eval);
    const dispatch = useDispatch();

    const [show, setShow] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);
    const [user, setUser] = useState(
        {
            firstName: props.evaluator ? props.evaluator.firstName :  "",
            lastName: props.evaluator ? props.evaluator.lastName : "",
            position: props.evaluator ? props.evaluator.position : "",
            email: props.evaluator ? props.evaluator.email : "",
            password: "",   /* evaluator ? evaluator.password :  */
            active: props.evaluator ? props.evaluator.active : false,
            roles: props.evaluator ? props.evaluator.roles : []
        });
   
    const createEvaluatorHandler = (user) => {
        dispatch(evaluatorActions.createEvaluator(user));
    }

    const createEvaluatorClearDataHandler = () => {
        dispatch(evaluatorActions.createEvaluatorClearDataAction());
    }

    useEffect(() => {
        const resSuccess = evalData.status === 201;
        setSuccess(resSuccess);
    }, [evalData.status, createEvaluatorHandler]);

    useEffect(() => {
        var isError = evalData.errorCreateUser !== null && evalData.errorCreateUser !== undefined;
        setError(isError);
    }, [evalData.errorCreateUser, createEvaluatorHandler]);

    const clearAllDataAfterCloseHandler = () => {
        createEvaluatorClearDataHandler();
        setShow(false);
        setError(false);
        setUser({
            firstName: "",
            lastName: "",
            position: "",
            email: "",
            password: "",
            active: false,
            roles: []
        })
        props.refresh();
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        createEvaluatorHandler(user);
        success ? setUser({
            firstName: "",
            lastName: "",
            position: "",
            email: "",
            password: "",
            active: false,
            roles: []
        }) : setUser(user);
    }
    const handleClose =(() => { clearAllDataAfterCloseHandler(); });
    const handleShow = () => setShow(true);
    const handleAddRoles = async (e) => {
        if (e.target.checked) {
            tmpRoles.add(e.target.value);
        } else if (tmpRoles.has(e.target.value) && !e.target.checked) {
            tmpRoles.delete(e.target.value);
        }
        user.roles = Array.from(tmpRoles);
    }

    if (error === true) {
        const serverErrorMessage = evalData.errorCreateUser + '';
        var errMessage = serverErrorMessage.split(',').map(str => <p>{str}</p>);
        errorMessage = (
            <Alert className="text-center mt-3" variant='danger'> {errMessage} </Alert>);
    }

    if(ev){
        buttonUpdateOrCreate = <Button variant="outline-secondary" className='rounded-circle ms-2' onClick={handleShow}> <TfiPencilAlt /> </Button>
    } else {
        buttonUpdateOrCreate = <Button variant="success" onClick={handleShow}>Add evaluater</Button>
    }

    formContent = (<Modal.Body className="modal-body d-flex flex-column">
        <Form className="form-signin">

            <h3 className="h2 mb-4 font-weight-normal d-flex justify-content-center align-items-center">Create new evaluater</h3>

            <Form.Label><b>New Evaluator Information</b></Form.Label>
            <Form.Control
                className="form-control"
                type="text"
                name='firstName'
                placeholder="First name"
                value={props.evaluator && !user.firstName ? props.evaluator.firstName : user.firstName}
                onChange={async (e) => { setUser({ ...user, firstName: e.target.value }); }}
                autoFocus />
            <Form.Control
                className="form-control"
                type="text"
                name='lastName'
                placeholder="Last Name"
                value={user.lastName}
                onChange={async (e) => { setUser({ ...user, lastName: e.target.value }); }} />
            <Form.Control
                className="form-control"
                type="text"
                name='position'
                placeholder="Position"
                value={user.position}
                onChange={async (e) => { setUser({ ...user, position: e.target.value }) }} />
            <Form.Control
                className="form-control"
                type="email"
                name='email'
                placeholder="Email"
                value={user.email}
                onChange={async (e) => { setUser({ ...user, email: e.target.value }); }}
                required />
            <Form.Control
                className="form-control"
                type="password"
                name='password'
                placeholder="Password"
                value={user.password}
                onChange={async (e) => { setUser({ ...user, password: e.target.value }); }}
                required />

            <Form.Label><b>Account Setup Information</b></Form.Label>
            <Form.Check
                type="checkbox"
                id="active"
                name="active"
                label="Active account"
                className="mb-2"
                checked={user.active}
                onChange={async (e) => { setUser({ ...user, active: e.target.checked }) }}>
            </Form.Check>

            <Form.Label><b>Role Account Information</b></Form.Label>
            {userRoles.map((role) => (
                <div key={role} className="mb-2">
                    <Form.Check
                        className="mb-2"
                        type='checkbox'
                        label={role}
                        value={role}
                        checked={(props.evaluator ? props.evaluator.roles.includes(role) : false)}
                        onChange={handleAddRoles}
                    />
                </div>
            ))}
        </Form>
        <Modal.Footer className="justify-content-center align-items-center">
            <Button
                className="mt-4"
                type="submit"
                variant="success"
                size="lg"
                onClick={handleSubmit}>
                Create
            </Button>
        </Modal.Footer>
        {errorMessage}
        <p className="d-flex justify-content-center align-items-center mt-4 mb-3 text-muted">&copy; 2023</p>
    </Modal.Body>);

    if (success === true) {
        formContent = (
            <Alert className="text-center m-4" key='success' variant='success'>
                <h1 className="cover-heading">The evaluater is successfully created!</h1>
                <Button variant="success" onClick={clearAllDataAfterCloseHandler}>Close</Button>
            </Alert>);
    }

    return (
        <>
            {buttonUpdateOrCreate}

            <Modal className="modal fade" show={show} onHide={handleClose} >
                <Modal.Header className="modal-header border-bottom-0 d-flex justify-content-center align-items-center" closeButton></Modal.Header>
                {formContent}
            </Modal>
        </>
    );

}

export default NewEvaluator;