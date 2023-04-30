create table [messages].[message_tracker](
                                             [id]                           int identity (1,1) not null,
                                             [sender_reference]             nvarchar(100)      not null,
                                             [message_type]                 nvarchar(16)       not null,
                                             [status]                       nvarchar(16)       not null,
                                             [request_date]                 datetime           not null,
                                             [response_date]                datetime           null,
                                             [request]                      nvarchar(max)      not null,
                                             [response]                     nvarchar(max)      null,
                                             [error_message]                nvarchar(max)      null,
                                             constraint [PK_message_tracker] primary key ([Id])
);

create index [IX_message_tracker_reference_number]
    on [messages].[message_tracker] ([sender_reference]);

create table messages.transaction_reference
(
    id                      int identity
                                constraint transaction_reference_pk
                                primary key,
    message_tracker_id      int                     not null
                                constraint transaction_reference_message_tracker_id_fk
                                references messages.message_tracker,
    transaction_reference   VARCHAR(16)             not null,
    status                  VARCHAR(30)             not null
);

create index [IX_transaction_reference_number]
    on [messages].[transaction_reference] ([transaction_reference]);
