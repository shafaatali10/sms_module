import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sms from './sms';
import SmsDetail from './sms-detail';
import SmsUpdate from './sms-update';
import SmsDeleteDialog from './sms-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SmsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SmsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SmsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sms} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SmsDeleteDialog} />
  </>
);

export default Routes;
