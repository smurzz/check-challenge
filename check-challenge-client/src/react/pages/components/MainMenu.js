import React from 'react';
import { Navbar, Nav, NavDropdown, Form, Button, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { useNavigate } from "react-router-dom";
import { VscAccount } from 'react-icons/vsc'
import '../../../layout/css/main-menu.css';

import * as authenticationActions from '../../../redux/authentication/AuthenticationAction';

const mapStateToPrors = state => {
  const { auth } = state
  return auth;
}

function MainMenu(props) {
  var userSession = JSON.parse(localStorage.getItem('userSession'));
  const navigate = useNavigate();

  const handleLogout = async (e) => {
    e.preventDefault();
    props.logoutUserAction();
    navigate('/');
  }

  if (userSession) {
    var logoutButton = (<Button variant="outline-success me-2" onClick={handleLogout}>Logout</Button>);
    var myAccontIcon = (<Button variant="secondary" className='rounded-circle me-2'> <VscAccount size={18} /> </Button>);
    var userRoles = userSession.user.roles;

    if (userRoles.includes("ADMIN")) {

      var linkToUsers = (
        <NavDropdown title="Users" id="navbarScrollingDropdown">
          <NavDropdown.Item href="/users">Evaluators</NavDropdown.Item>
          <NavDropdown.Item href="#action4"> Applicants </NavDropdown.Item>
        </NavDropdown>
      );

    }
  } else {
    var loginButton = (<Link to="/login"> <Button variant="outline-success me-2">Log in</Button> </Link>);
    var signupButton = (<Link to="/register"> <Button variant="success" >Sign up</Button> </Link>);
  }

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container fluid>
        <Navbar.Brand>CChallenge</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: '100px' }}
            navbarScroll>
            <Nav.Link href={userSession ? "/home" : "/"}>Home</Nav.Link>
            {linkToUsers}
          </Nav>
          <Form className="d-flex">
            {loginButton}
            {signupButton}
            {myAccontIcon}
            {logoutButton}
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

const mapDispatchToProps = dispatch => {
  return {
    logoutUserAction: () => dispatch(authenticationActions.logoutUser())
  }
}

const ConnectedLogoutButton = connect(mapStateToPrors, mapDispatchToProps)(MainMenu)
export default ConnectedLogoutButton;