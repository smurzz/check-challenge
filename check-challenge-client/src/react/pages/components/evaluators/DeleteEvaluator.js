import React, { useState } from "react";
import { Modal, Button } from 'react-bootstrap';
import { TfiTrash } from "react-icons/tfi";
import { useDispatch } from 'react-redux';

import * as evaluatorActions from '../../../../redux/user/EvaluatorAction';

function DeleteEvaluatorWidget({ evaluator, refresh }) {
    const  email = evaluator.email;
    const fullNameEvaluator = evaluator.firstName + " " + evaluator.lastName;

    const dispatch = useDispatch();

    const deleteEvaluatorHandler = (email) => {
        dispatch(evaluatorActions.deleteEvaluator(email));
    }

    const [show, setShow] = useState(false);
    const handleClose = (() => { setShow(false); refresh(); });
    const handleShow = () => setShow(true);

    const handleSubmit = (e) => {
        deleteEvaluatorHandler(email);
        handleClose();
    }

    return (
        <>
            <Button variant="outline-danger" className='rounded-circle ms-2' onClick={handleShow}> <TfiTrash /> </Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Are you sure that you want to delete evaluator {fullNameEvaluator}? </Modal.Title>
                </Modal.Header>
                <Modal.Body>The user will be permanently removed from the database.</Modal.Body>
                <Modal.Footer>
                    <Button id="DeleteUserCancel" variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button id="DeleteUserConfirm" variant="primary" onClick={handleSubmit}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
export default DeleteEvaluatorWidget;