import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './sms.reducer';
import { ISms } from 'app/shared/model/sms.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISmsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SmsUpdate = (props: ISmsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { smsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/sms' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dateTime = convertDateTimeToServer(values.dateTime);

    if (errors.length === 0) {
      const entity = {
        ...smsEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smsModuleApp.sms.home.createOrEditLabel">Create or edit a Sms</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : smsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="sms-id">ID</Label>
                  <AvInput id="sms-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="statusLabel" for="sms-status">
                  Status
                </Label>
                <AvField id="sms-status" type="text" name="status" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneNumberLabel" for="sms-phoneNumber">
                  Phone Number
                </Label>
                <AvField id="sms-phoneNumber" type="text" name="phoneNumber" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneNameLabel" for="sms-phoneName">
                  Phone Name
                </Label>
                <AvField id="sms-phoneName" type="text" name="phoneName" />
              </AvGroup>
              <AvGroup>
                <Label id="dateTimeLabel" for="sms-dateTime">
                  Date Time
                </Label>
                <AvInput
                  id="sms-dateTime"
                  type="datetime-local"
                  className="form-control"
                  name="dateTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.smsEntity.dateTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="messageLabel" for="sms-message">
                  Message
                </Label>
                <AvField id="sms-message" type="text" name="message" />
              </AvGroup>
              <AvGroup check>
                <Label id="isPinnedLabel">
                  <AvInput id="sms-isPinned" type="checkbox" className="form-check-input" name="isPinned" />
                  Is Pinned
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/sms" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  smsEntity: storeState.sms.entity,
  loading: storeState.sms.loading,
  updating: storeState.sms.updating,
  updateSuccess: storeState.sms.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SmsUpdate);
