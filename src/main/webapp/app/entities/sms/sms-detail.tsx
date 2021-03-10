import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sms.reducer';
import { ISms } from 'app/shared/model/sms.model';
import {
  APP_DATE_FORMAT,
  APP_LOCAL_DATE_FORMAT,
  APP_LOCAL_DATETIME_FORMAT,
  APP_TIMESTAMP_FORMAT
} from 'app/config/constants';

export interface ISmsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SmsDetail = (props: ISmsDetailProps) => {

  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { smsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          SMS
        </h2>
        <h5>
          from- <b>{smsEntity.phoneNumber}</b> @ <b>{smsEntity.dateTime ? <TextFormat value={smsEntity.dateTime} type="date" format={APP_TIMESTAMP_FORMAT} /> : null}</b>
        </h5>
        <dl className="jh-entity-details">
          {/*<dt>
            <span id="status">Status</span>
          </dt>
          <dd>{smsEntity.status}</dd>
          <dt>
            <span id="phoneNumber">Phone Number</span>
          </dt>
          <dd>{smsEntity.phoneNumber}</dd>
          <dt>
            <span id="phoneName">Phone Name</span>
          </dt>
          <dd>{smsEntity.phoneName}</dd>
          <dt>
            <span id="dateTime">Date Time</span>
          </dt>
          <dd>{smsEntity.dateTime ? <TextFormat value={smsEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>*/}
          {/*<dt>
            <span id="message">Message</span>
          </dt>*/}
          <dd>{smsEntity.message}</dd>
          {/*<dt>
            <span id="isPinned">Is Pinned</span>
          </dt>
          <dd>{smsEntity.isPinned ? 'true' : 'false'}</dd>*/}
        </dl>
        <Button tag={Link} to="/sms" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        {/*<Button tag={Link} to={`/sms/${smsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>*/}
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ sms }: IRootState) => ({
  smsEntity: sms.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SmsDetail);
