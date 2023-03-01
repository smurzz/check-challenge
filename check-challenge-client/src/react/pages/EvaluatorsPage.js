import React, { useEffect, useState } from 'react';
import MainMenu from './components/MainMenu';
import { ListGroup, Table } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from "redux";
import * as evaluaterActions from '../../redux/user/EvaluatorAction';
import CreateUpdateEvaluator from './components/evaluators/CreateUpdateEvaluator';
import DeleteEvaluator from './components/evaluators/DeleteEvaluator';

import '../../layout/css/users-page.css';

const mapStateToPrors = state => {
    return {
        evaluatorData: state.evaluator
    };
}

function EvaluatorsPage({ evaluatorData, getEvaluatorsAction }) {

    const [counter, updateCounter] = useState(0);

    useEffect(() => {
        getEvaluatorsAction();
    }, [counter, getEvaluatorsAction]);

    const incrementCounter = () => {
        updateCounter(counter + 1);
    }

    const roles = [];
    roles.push('ADMIN', 'USER');

    return (
        evaluatorData.errorGetUsers ? (
            <dev className="d-flex h-100 mx-auto flex-column">
                < MainMenu />
                <h2 className="display-6 text-center mt-4">{evaluatorData.errorGetUsers}</h2>
            </dev>
        ) : (
            <dev className="d-flex h-100 mx-auto flex-column">
                < MainMenu />
                <div className="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
                    <h1 className="display-4">Evaluators</h1>
                    <p className="lead">Endava employees who evaluate java job seekers' challenges.</p>
                    < CreateUpdateEvaluator refresh={incrementCounter} />
                </div>
                <div className="container  pb-4">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="lead text-uppercase mb-0">Manage Evaluators</h5>
                                </div>
                                <div className="table-responsive">
                                    <Table responsive className="table no-wrap user-table mb-0">
                                        <thead>
                                            <tr>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium pl-4">#</th>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium">Name</th>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium">Occupation</th>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium">Email</th>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium">Roles</th>
                                                <th scope="col" className="lead border-0 text-uppercase font-medium">Manage</th>
                                            </tr>
                                        </thead>
                                        <tbody >
                                            {evaluatorData && evaluatorData.evaluators && Object.values(evaluatorData.evaluators)
                                                .map((evaluator, index) => {
                                                    return <tr key={evaluator.id}>
                                                        <td className="pl-4">{index + 1}</td>
                                                        <td>
                                                            <h5 className="font-medium mb-0">{evaluator.firstName} {evaluator.lastName}</h5>
                                                        </td>
                                                        <td>
                                                            <span className="lead text-muted">{evaluator.position}</span><br />
                                                        </td>
                                                        <td>
                                                            <span className="lead text-muted">{evaluator.email}</span><br />
                                                        </td>
                                                        <td>
                                                            <ListGroup as="ol" numbered>
                                                                {evaluator && evaluator.roles && evaluator.roles.map( (role) => {
                                                                    return <ListGroup.Item as="li" key={role.name}>{role.name}</ListGroup.Item>;
                                                                })}
                                                            </ListGroup>
                                                        </td>
                                                        <td>
                                                            <CreateUpdateEvaluator evaluator={evaluator} refresh={incrementCounter}/>
                                                            <DeleteEvaluator evaluator={evaluator} refresh={incrementCounter}/>
                                                        </td>
                                                    </tr>
                                                })
                                            }
                                        </tbody>
                                    </Table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </dev>
        )
    );
}
const mapDispatchToProps = dispatch => bindActionCreators({
    getEvaluatorsAction: evaluaterActions.getEvaluators
}, dispatch)

const ConnectedEvaluatorsPage = connect(mapStateToPrors, mapDispatchToProps)(EvaluatorsPage)

export default ConnectedEvaluatorsPage;