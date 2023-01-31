import React from 'react';
import MainMenu from './components/MainMenu';
import HomePrivate from './HomePrivate';

import { Container } from 'react-bootstrap';
import '../../layout/css/home-page.css';

function HomePublic() {

    const userSession = localStorage.getItem('userSession');

    return (
        !userSession ? (<dev className="d-flex h-100 mx-auto flex-column">
            < MainMenu />
            <Container className='cover-container mb-auto mt-auto w-50'>
                <main role="main" className="inner cover">
                    <h1 className="cover-heading">Check Java-challenges.</h1>
                    <p className="lead">Register as an Endava employee and evaluate the challenges' solutions of the Java-developer applicants.</p>
                </main>
            </Container>
        </dev>) : <HomePrivate />
    );
}

export default HomePublic;