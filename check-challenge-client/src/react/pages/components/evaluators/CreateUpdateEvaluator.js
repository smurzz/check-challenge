import React, { useState, useEffect, useCallback } from "react";
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { TfiPencilAlt } from "react-icons/tfi";

import * as evaluatorActions from '../../../../redux/user/EvaluatorAction';

function CreateUpdateEvaluator({ refresh, evaluator }) {

    const [show, setShow] = useState(false);
    const [success, setSuccess] = useState(false);
    const [user, setUser] = useState(evaluator || {});
    const evalData = useSelector(state => state.evaluator);
    const dispatch = useDispatch();
    const userRoles = [{ name: "USER" }, { name: "ADMIN" }];
    var content;
    
    useEffect(() => {
        setUser(evaluator || {});
    }, [evaluator]);

    const createEvaluator = useCallback(() => {
        dispatch(evaluatorActions.createEvaluator(user));
    }, [dispatch, user])

    const editEvaluator = (email, user) => dispatch(evaluatorActions.editEvaluator(email, user));
    const clearEvalData = () => dispatch(evaluatorActions.clearDataAction());

    useEffect(() => {
        const resSuccess = evalData.status === 201 || evalData.status === 204;
        setSuccess(resSuccess);
    }, [evalData.status, createEvaluator]);

    const handleShow = () => setShow(true);
    const handleClose = () => {
        clearEvalData();
        setShow(false);
        setUser({});
        refresh();
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userRequest = (evaluator && (({ id, authorities, ...rest }) => rest)(user)) || {};
        evaluator ? editEvaluator(userRequest.email, userRequest) : createEvaluator(user);
        success ? setUser({}) : setUser(user);
    }

    const handleAddRoles = async (e) => {
        let updatedRoles = user.roles ? [...user.roles] : [];
        let isRolesEmpty = updatedRoles.length === 0;
        let rolesHasValue = updatedRoles.some(r => r.name !== e.target.value);

        if (e.target.checked && (isRolesEmpty || rolesHasValue)) {
            updatedRoles.push({ name: e.target.value });
        } else {
            updatedRoles = updatedRoles.filter(r => r.name !== e.target.value);
        }
        setUser({ ...user, roles: updatedRoles });
    }

    const errorMessage = evalData.error && (
        <Alert className="text-center mt-3" variant='danger'>
            {evalData.error.split(',').map(str => <p>{str}</p>)}
        </Alert>);


    const button = evaluator ? (
        <Button variant="outline-secondary" className='rounded-circle' onClick={handleShow}>
            <TfiPencilAlt />
        </Button>
    ) : (
        <Button variant="success" onClick={handleShow}>Add evaluater</Button>
    );

    if (evalData.status === 201) {
        content = <Alert className="text-center m-4" key='success' variant='success'>
            <h1 className="cover-heading">
                The evaluater is successfully created!
            </h1>
            <Button variant="success" onClick={handleClose}>Close</Button>
        </Alert>
    } else if (evalData.status === 204) {
        content = <Alert className="text-center m-4" key='success' variant='success'>
            <h1 className="cover-heading">
                The changes are successfuly saved!
            </h1>
            <Button variant="success" onClick={handleClose}>Close</Button>
        </Alert>
    } else {
        content = <Modal.Body className="modal-body d-flex flex-column">
            <Form className="form-signin">

                <h3 className="h2 mb-4 font-weight-normal d-flex justify-content-center align-items-center">
                    {evaluator ? "Update Evaluator" : "Create Evaluator"}
                </h3>

                <Form.Label><b>Evaluator Information</b></Form.Label>
                <Form.Control
                    className="form-control"
                    type="text"
                    name='firstName'
                    placeholder="First name"
                    value={user.firstName || ''}
                    onChange={async (e) => { setUser({ ...user, firstName: e.target.value }); }}
                    autoFocus />
                <Form.Control
                    className="form-control"
                    type="text"
                    name='lastName'
                    placeholder="Last Name"
                    value={user.lastName || ''}
                    onChange={async (e) => { setUser({ ...user, lastName: e.target.value }); }} />
                <Form.Control
                    className="form-control"
                    type="text"
                    name='position'
                    placeholder="Position"
                    value={user.position || ''}
                    onChange={async (e) => { setUser({ ...user, position: e.target.value }) }} />
                <Form.Control
                    className="form-control"
                    disabled={evaluator}
                    type="email"
                    name='email'
                    placeholder="Email"
                    value={user.email || ''}
                    onChange={async (e) => { setUser({ ...user, email: e.target.value }); }}
                    required={!evaluator} />
                <Form.Control
                    className="form-control"
                    type="password"
                    name='password'
                    placeholder="Password"
                    value={user.password || ''}
                    onChange={async (e) => { setUser({ ...user, password: e.target.value }); }}
                    required={!evaluator} />

                <Form.Label><b>Account Setup Information</b></Form.Label>
                <Form.Check
                    type="checkbox"
                    id="active"
                    name="active"
                    label="Active account"
                    className="mb-2"
                    checked={user.active || false}
                    onChange={async (e) => { setUser({ ...user, active: e.target.checked }) }}>
                </Form.Check>

                <Form.Label><b>Role Account Information</b></Form.Label>
                {userRoles.map((role) => (
                    <div key={role.name} className="mb-2">
                        <Form.Check
                            className="mb-2"
                            type='checkbox'
                            label={role.name}
                            value={role.name}
                            checked={(user.roles && user.roles.some(r => r.name === role.name)) || false}
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
                    Save
                </Button>
            </Modal.Footer>
            {errorMessage}
            <p className="d-flex justify-content-center align-items-center mt-4 mb-3 text-muted">&copy; 2023</p>
        </Modal.Body>
    }

    return (
        <>
            {button}

            <Modal className="modal fade" show={show} onHide={handleClose} >
                <Modal.Header className="modal-header border-bottom-0 d-flex justify-content-center align-items-center" closeButton></Modal.Header>
                {content}
            </Modal>
        </>
    );

}

export default CreateUpdateEvaluator;