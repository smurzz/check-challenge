import React, { useState, useEffect, useCallback } from "react";
import { useLocation } from "react-router-dom";
import { useDispatch, useSelector } from 'react-redux';
import { Form, Container, Card, Button, Alert } from 'react-bootstrap';
import MainMenu from './components/MainMenu';

import * as evaluationActions from '../../redux/evaluation/EvaluationAction';

import '../../layout/css/home-page.css'

function EvaluatePage() {
    const location = useLocation();
    const evaluationData = useSelector(state => state.evaluation);
    const dispatch = useDispatch();

    const challenge = location.state.data;
    const userSession = JSON.parse(localStorage.getItem('userSession'));
    const evaluaterEmail = userSession.user.sub;

    const [evaluation, setEvaluation] = useState(
        {
            implementationCriteria: "0",
            algorithmicCriteria: "0",
            structureCriteria: "0",
            errorHandlingCriteria: "0",
            formattingCriteria: "0",
            commitHistoryCriteria: "0",
            readmeCriteria: "0",
            testQualityCriteria: "0",
            designPatternsCriteria: "0",
            stylingCriteria: "0",
            ciCdCriteria: "0",
            dockerCriteria: "0",
            challengeId: challenge.id,
            userEmail: evaluaterEmail
        });
    const [result, setResult] = useState();
    const maxScore = 30;

    const createEvaluation = useCallback(() => {
            dispatch(evaluationActions.createEvaluatoion(evaluation));
    }, [dispatch, evaluation]);

    useEffect(() => {
        var interimResult = parseInt(evaluation.implementationCriteria) + parseInt(evaluation.algorithmicCriteria) + parseInt(evaluation.structureCriteria)
        + parseInt(evaluation.errorHandlingCriteria) + parseInt(evaluation.formattingCriteria) + parseInt(evaluation.commitHistoryCriteria)
        + parseInt(evaluation.readmeCriteria) + parseInt(evaluation.testQualityCriteria) + parseInt(evaluation.designPatternsCriteria)
        + parseInt(evaluation.stylingCriteria) + parseInt(evaluation.ciCdCriteria) + parseInt(evaluation.dockerCriteria);
        setResult((interimResult / maxScore) * 100);
    }, [evaluation])

    const handleSubmit = async (e) => {
        e.preventDefault();
        createEvaluation(evaluation);
        (evaluationData.status === 201) ? setEvaluation({}) : setEvaluation(evaluation);
        console.log(evaluation);
    }

    const errorMessage = evaluationData.error && (
        <Alert className="text-center mt-3" variant='danger'>
            {evaluationData.error.split(',').map(str => <p>{str}</p>)}
        </Alert>);

    const successMessage = evaluationData.status === 201 && (
        <Alert className="text-center mt-3" variant='success'>
            Your evaluation is successfully saved! The candidate has received an evaluation of {result.toFixed(1) > 100 ? 100 : result.toFixed(1)}% 
            and a score of {(result / 100 * 5).toFixed(1) > 5 ? 5 : (result / 100 * 5).toFixed(1)} out of 5.
        </Alert>);

    return (
        <dev className="d-flex h-100 mx-auto flex-column">
            < MainMenu />
            <div className="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
                <h1 className="display-4">Evaluate Form</h1>
                <p className="lead">Please evaluate the challenge-task according to the following criteria.
                    Link to the repository is <a href={challenge.html_url}>here</a>. </p>
            </div>
            <Container className='px-3 py-3 pb-md-4 mx-auto'>
                <Card className="mandatoryCategories mb-3">
                    <Card.Header> <h3 className="display-8">Mandatory - 20</h3> </Card.Header>
                    <Card.Body>
                        <Form>
                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Implementation criteria</b></Form.FloatingLabel>
                                <Form.Text>All of the acceptance criteria or application components defined for the task
                                    have been implemented by the candidate.</Form.Text> <br />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.implementationCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.implementationCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.implementationCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="3"
                                    type='radio'
                                    value={3}
                                    checked={evaluation.implementationCriteria === '3'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="4"
                                    type='radio'
                                    value={4}
                                    checked={evaluation.implementationCriteria === '4'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='implementationCriteria'
                                    label="5"
                                    type='radio'
                                    value={5}
                                    checked={evaluation.implementationCriteria === '5'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, implementationCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Algorithmic criteria</b></Form.FloatingLabel>
                                <Form.Text>Efficiency and accuracy of the algorithms, the appropriate use of data structures,
                                    and the clarity and organization of the code.</Form.Text> <br />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.algorithmicCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.algorithmicCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.algorithmicCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="3"
                                    type='radio'
                                    value={3}
                                    checked={evaluation.algorithmicCriteria === '3'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="4"
                                    type='radio'
                                    value={4}
                                    checked={evaluation.algorithmicCriteria === '4'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="5"
                                    type='radio'
                                    value={5}
                                    checked={evaluation.algorithmicCriteria === '5'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="6"
                                    type='radio'
                                    value={6}
                                    checked={evaluation.algorithmicCriteria === '6'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="7"
                                    type='radio'
                                    value={7}
                                    checked={evaluation.algorithmicCriteria === '7'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="8"
                                    type='radio'
                                    value={8}
                                    checked={evaluation.algorithmicCriteria === '8'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="9"
                                    type='radio'
                                    value={9}
                                    checked={evaluation.algorithmicCriteria === '9'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='algorithmicCriteria'
                                    label="10"
                                    type='radio'
                                    value={10}
                                    checked={evaluation.algorithmicCriteria === '10'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, algorithmicCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Structure criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to organize and structure the codebase
                                    in such a way that it is easy for other team members to maintain and understand.</Form.Text> <br />
                                <Form.Check
                                    name='structureCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.structureCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, structureCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='structureCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.structureCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, structureCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='structureCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.structureCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, structureCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Error Handling criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to handle errors or exceptions that may occur 
                                    during the execution of a program.</Form.Text> <br />
                                <Form.Check
                                    name='errorHandlingCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.errorHandlingCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, errorHandlingCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='errorHandlingCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.errorHandlingCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, errorHandlingCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='errorHandlingCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.errorHandlingCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, errorHandlingCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Formatting criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to write code that is easy to understand, maintain, 
                                    and work with in a professional setting.</Form.Text> <br />
                                <Form.Check
                                    name='formattingCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.formattingCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, formattingCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='formattingCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.formattingCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, formattingCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>
                        </Form>
                    </Card.Body>
                </Card>

                <Card className="advancedCategories mb-3">
                    <Card.Header> <h3 className="display-8">Advanced - 10</h3> </Card.Header>
                    <Card.Body>
                        <Form>
                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Commit History criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's technical skills, that provide valuable insights 
                                    into their development practices, overall approach to software development.</Form.Text> <br />
                                <Form.Check
                                    name='commitHistoryCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.commitHistoryCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, commitHistoryCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='commitHistoryCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.commitHistoryCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, commitHistoryCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='commitHistoryCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.commitHistoryCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, commitHistoryCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Readme criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to provide clear and informative documentation for their code, 
                                    including instructions for installation and setup.</Form.Text> <br />
                                <Form.Check
                                    name='readmeCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.readmeCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, readmeCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='readmeCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.readmeCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, readmeCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='readmeCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.readmeCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, readmeCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='readmeCriteria'
                                    label="3"
                                    type='radio'
                                    value={3}
                                    checked={evaluation.readmeCriteria === '3'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, readmeCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Test Quality criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to write effective and comprehensive tests for their code.</Form.Text> <br />
                                <Form.Check
                                    name='testQualityCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.testQualityCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, testQualityCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='testQualityCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.testQualityCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, testQualityCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='testQualityCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.testQualityCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, testQualityCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='testQualityCriteria'
                                    label="3"
                                    type='radio'
                                    value={3}
                                    checked={evaluation.testQualityCriteria === '3'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, testQualityCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Design Patterns criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's knowledge and understanding of common design patterns, 
                                    and their ability to apply those patterns in their code.</Form.Text> <br />
                                <Form.Check
                                    name='designPatternsCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.designPatternsCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, designPatternsCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='designPatternsCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.designPatternsCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, designPatternsCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='designPatternsCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.designPatternsCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, designPatternsCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>
                        </Form>
                    </Card.Body>
                </Card>

                <Card className="bonusCategories mb-3">
                    <Card.Header> <h3 className="display-8">Bonus - 5</h3> </Card.Header>
                    <Card.Body>
                        <Form>
                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Styling criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to write well-styled and organized code 
                                    that is easy to read and understand.</Form.Text> <br />
                                <Form.Check
                                    name='stylingCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.stylingCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, stylingCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='stylingCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.stylingCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, stylingCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>CI/CD Pipeline criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to set up and use automated processes 
                                    and tools to improve the efficiency and reliability of the development process.</Form.Text> <br />
                                <Form.Check
                                    name='ciCdCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.ciCdCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, ciCdCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='ciCdCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.ciCdCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, ciCdCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='ciCdCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.ciCdCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, ciCdCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>

                            <Form.Group className="mb-2">
                                <Form.FloatingLabel><b>Docker Image criteria</b></Form.FloatingLabel>
                                <Form.Text>The candidate's ability to create, configure, and use Docker images in their development workflow.</Form.Text> <br />
                                <Form.Check
                                    name='dockerCriteria'
                                    label="0"
                                    type='radio'
                                    value={0}
                                    checked={evaluation.dockerCriteria === '0'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, dockerCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='dockerCriteria'
                                    label="1"
                                    type='radio'
                                    value={1}
                                    checked={evaluation.dockerCriteria === '1'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, dockerCriteria: e.target.value }) }}
                                    inline />
                                <Form.Check
                                    name='dockerCriteria'
                                    label="2"
                                    type='radio'
                                    value={2}
                                    checked={evaluation.dockerCriteria === '2'}
                                    onChange={async (e) => { setEvaluation({ ...evaluation, dockerCriteria: e.target.value }) }}
                                    inline />
                            </Form.Group>
                        </Form>
                    </Card.Body>
                </Card>

                {errorMessage}
                {successMessage}

                <Button variant="success" size="lg" onClick={handleSubmit}>Save</Button>
            </Container>

        </dev>
    );
}

export default EvaluatePage;