import React from 'react';
import MainMenu from './components/MainMenu';
import AllChallenges from './components/challenges/AllChallenges';
import InProcessingChallenges from './components/challenges/InProcessingChallenges';
import CompletedChallenges from './components/challenges/CompletedChallenges';
import UnevaluatedChallenges from './components/challenges/UnevaluatedChallenges';

import { Container, Tab, Tabs } from 'react-bootstrap';
import '../../layout/css/home-page.css'

function HomePrivate() {
    return (
        <dev className="d-flex h-100 mx-auto flex-column">
            < MainMenu />
            <div className="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
                <h1 className="display-4">Challenges</h1>
                <p className="lead">Challenges solutions from java-developer applicants for evaluation.</p>
            </div>
            <Container className='d-flex flex-column h-100 mb-auto w-50'>
                <Tabs
                    defaultActiveKey="all"
                    transition={false}
                    id="noanim-tab-example"
                    className="mb-3"
                >
                    <Tab eventKey="all" title="All">
                        <AllChallenges />
                    </Tab>
                    <Tab eventKey="unevaluated" title="Unevaluated">
                        <UnevaluatedChallenges />
                    </Tab>
                    <Tab eventKey="in-processing" title="In processing">
                        <InProcessingChallenges />
                    </Tab>
                    <Tab eventKey="completed" title="Completed">
                        <CompletedChallenges />
                    </Tab>
                </Tabs>
            </Container>
        </dev>
    );
}

export default HomePrivate;