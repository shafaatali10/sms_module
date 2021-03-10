import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './config.reducer';
import { IConfig } from 'app/shared/model/config.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IConfigUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ConfigUpdate = (props: IConfigUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { configEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/config' + props.location.search);
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
    if (errors.length === 0) {
      const entity = {
        ...configEntity,
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
          <h2 id="smsModuleApp.config.home.createOrEditLabel">App Configuration</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : configEntity} onSubmit={saveEntity}>
              {/*{!isNew ? (
                <AvGroup>
                  <Label for="config-id">ID</Label>
                  <AvInput id="config-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}*/}
              <AvGroup>
                <Label id="configNameLabel" for="config-configName">
                  Config Name
                </Label>
                <AvField id="config-configName" type="text" name="configName" readOnly={!isNew}/>
              </AvGroup>
              <AvGroup>
                <Label id="configValueLabel" for="config-configValue">
                  Config Value
                </Label>
                <AvField id="config-configValue" type="text" name="configValue" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="config-description">
                  Description
                </Label>
                <AvField id="config-description" type="text" name="description" readOnly={!isNew} />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/config" replace color="info">
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
  configEntity: storeState.config.entity,
  loading: storeState.config.loading,
  updating: storeState.config.updating,
  updateSuccess: storeState.config.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ConfigUpdate);
