import React from 'react';
import { Button, Card, Container } from 'react-bootstrap';

function InProcessingChallenges() {

    return (
        <Container className='pb-4'>
            <Card className='mb-3' border='warning'>
            <Card.Header as="h5"><a href='#' class="text-dark" style={{ textDecoration: 'none' }}>Challenage_N18_Tante_Amalie</a></Card.Header>
                <Card.Body>
                <Card.Title>Senior Software Developer</Card.Title>
                    <Card.Text> Link to repository: 
                        <Card.Link href='#'> https://github.com/challenage_N18_Tante_Amalie </Card.Link> 
                    </Card.Text>
                    <Button variant="success">Evaluate</Button>
                    <Button variant="outline-success" className='ms-3'>View</Button>
                </Card.Body>
                <Card.Footer style={{textAlign: "right"}}>Score: <b>-</b></Card.Footer>
            </Card>
        </Container>
    );
}

export default InProcessingChallenges;