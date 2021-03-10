import './home.scss';
import Image from './iphone-message.jpg';

import React from 'react';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;

  return (
    <Row>
      <Col md="9">

        {account && account.login ? (
          <div>
            <h2>Welcome, {account.firstName}</h2>
            <br/>
            <p className="lead">Here you can configure and view your SMS sent to your GSM sim </p>
            <br/>
            <Alert color="success">You are logged in as user {account.login}.</Alert>
          </div>
        ) : (
          <div>
            <h2>Welcome, kindly sign in to use this app!</h2>
            <br/>
            <p className="lead">Here you can configure and view your SMS sent to your GSM sim </p>
            <br/>
            <Alert color="warning">
              If you want to sign in
              {/*<Link to="/login" classNam e="alert-link">*/}
              {/*  {' '}*/}
              {/*  */}
              {/*</Link>*/}
                , <Link to="/login" className="alert-link">
              {' '}
              click here
            </Link>
            </Alert>

            <Alert color="warning">
              You do not have an account yet?&nbsp;
              Contact the owner of this app.
            </Alert>
          </div>
        )}

      </Col>
      <Col md="3" className="pad">
        <span className="rounded float-right" > <img src={Image} style={{height: '550px', margin: '0 auto'}}/> </span>
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
