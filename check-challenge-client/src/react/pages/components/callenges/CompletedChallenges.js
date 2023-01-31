import React from 'react';
import { Button, Card, Container } from 'react-bootstrap';

function CompletedChallenges() {

    return (
        <Container className='pb-4'>

            <Card className='mb-3' border='success'>
            <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>Challenage_N7_John_Smith</a></Card.Header>
                <Card.Body>
                    <Card.Title>Developer</Card.Title>
                    <Card.Text> Link to repository: <Card.Link href='#'> https://github.com/challenage_N7_John_Smith </Card.Link> 
                    </Card.Text>
                    <Button variant="success" disabled>Evaluate</Button>
                    <Button variant="outline-success" className='ms-3'>View</Button>
                </Card.Body>
                <Card.Footer style={{textAlign: "right"}}>Score: <b>4.5</b></Card.Footer>
            </Card>

            <Card className='mb-3' border='success'>
            <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>Challenage_N11_Yamada_Hanako</a></Card.Header>
                <Card.Body>
                    <Card.Title>Java Software Engineer</Card.Title>
                    <Card.Text> Link to repository: <Card.Link href='#'> https://github.com/challenage_N11_Yamada_Hanako </Card.Link> 
                    </Card.Text>
                    <Button variant="success" disabled>Evaluate</Button>
                    <Button variant="outline-success" className='ms-3'>View</Button>
                </Card.Body>
                <Card.Footer style={{textAlign: "right"}}>Score: <b>3.0</b></Card.Footer>
            </Card>

        </Container>
    );
}

export default CompletedChallenges;