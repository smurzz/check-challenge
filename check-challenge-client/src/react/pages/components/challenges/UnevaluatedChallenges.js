import React, { useEffect } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { Button, Card, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';

import * as challengesAction from '../../../../redux/challenges/ChallengeAction';

function UnevaluatedChallenges() {

        const chalData = useSelector(state => state.challenge);
        const dispatch = useDispatch();
    
        useEffect(() => {
            dispatch(challengesAction.getChallenges());
        }, [dispatch])
    
        return (
            chalData.error ? (
                <dev className="d-flex h-100 mx-auto flex-column">
                    <h2 className="display-6 text-center mt-4">{chalData.error}</h2>
                </dev>
            ) : (
            <Container className='pb-4'>
    
                {chalData && chalData.challenges && Object.values(chalData.challenges)
                    .filter(challenge => challenge.unevaluated === true)
                    .map((challenge) => {
                        const dateCreation = new Date(challenge.created_at);
                        const dateUpdating = new Date(challenge.updated_at);
                        const maxScore = 30;
                        const score = challenge.averageScore ? (challenge.averageScore / maxScore * 5).toFixed(1) : 0;

                        return <Card className='mb-3' border='secondary' key={challenge.id}>
                        <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>{challenge.name}</a></Card.Header>
                        <Card.Body>
                            <Card.Text> {challenge.description} </Card.Text>
                            <Card.Title> Link to repository: </Card.Title>
                            <Card.Text> <Card.Link href={challenge.html_url}> {challenge.html_url} </Card.Link> </Card.Text>
                            <Card.Text className="text-muted">Created at: {dateCreation.toDateString()} <br />
                                Updated at: {dateUpdating.toDateString()} </Card.Text>
                            <Link to={'/evaluate/' + challenge.id} state={{ data: challenge } }><Button variant="success">Evaluate</Button></Link>
                            <Button variant="outline-success" className='ms-3'>View</Button>
                        </Card.Body>
                        <Card.Footer style={{textAlign: "right"}}>Score: <b>{score}</b></Card.Footer>
                    </Card>
                    })
                }
    
            </Container> )
        );
}

export default UnevaluatedChallenges;