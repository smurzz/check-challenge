import React from 'react';
import { Button, Card, Container } from 'react-bootstrap';

function UnevaluatedChallenges() {

    return (
        <Container className='pb-4'>
            <Card className='mb-3' border='secondary'>
                <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>Challenge_N2_Max_Mustermann</a></Card.Header>
                <Card.Body>
                    <Card.Title>Java Developer</Card.Title>
                    <Card.Text> Link to repository: <Card.Link href='#'> https://github.com/challenge_N2_Max_Mustermann </Card.Link> 
                    </Card.Text>
                    <Button variant="success">Evaluate</Button>
                    <Button variant="outline-success" className='ms-3'>View</Button>
                </Card.Body>
                <Card.Footer style={{textAlign: "right"}}>Score: <b>-</b></Card.Footer>
            </Card>

            <Card className='mb-3' border='secondary'>
            <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>Challenge_N7_Kari_Nordmann</a></Card.Header>
                <Card.Body>
                    <Card.Title>Developer backend</Card.Title>
                    <Card.Text> Link to repository: <Card.Link href='#'> https://github.com/challenge_N7_Kari_Nordmann </Card.Link> 
                    </Card.Text>
                    <Button variant="success">Evaluate</Button>
                    <Button variant="outline-success" className='ms-3'>View</Button>
                </Card.Body>
                <Card.Footer style={{textAlign: "right"}}>Score: <b>-</b></Card.Footer>
            </Card>
            
        </Container>
    );
}

export default UnevaluatedChallenges;