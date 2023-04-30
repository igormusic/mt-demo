create table [messages].[message_tracker](
                                             [id]                           int identity (1,1) not null,
                                             [transaction_reference_number] nvarchar(100)      not null,
                                             [message_type]                 nvarchar(16)       not null,
                                             [status]                       nvarchar(16)       not null,
                                             [request_date]                 datetime           not null,
                                             [response_date]                datetime           null,
                                             [request]                      nvarchar(max)      not null,
                                             [response]                     nvarchar(max)      null,
                                             [error_message]                nvarchar(max)      null,
                                             constraint [PK_message_tracker] primary key ([Id])
);

create index [IX_message_tracker_transaction_reference_number]
    on [messages].[message_tracker] ([transaction_reference_number]);

create table [messages].[message_tracker_history]
(
    [id]                           int identity (1,1) not null,
    [transaction_reference_number] nvarchar(100)      not null,
    [message_type]                 nvarchar(16)       not null,
    [status]                       nvarchar(16)       not null,
    [request_date]                 datetime           not null,
    [response_date]                datetime           null,
    [request]                      nvarchar(max)      not null,
    [response]                     nvarchar(max)      null,
    [error_message]                nvarchar(max)      null,
    constraint [PK_message_tracker_history] primary key ([Id])
);

create index [IX_message_tracker_history_transaction_reference_number]
    on [messages].[message_tracker_history] ([transaction_reference_number]);
