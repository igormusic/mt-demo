create table [messages].[message_tracker] (
    [id] int identity(1,1) not null,
    [request_transaction_reference_number] nvarchar(100) not null,
    [message_type] nvarchar(16) not null,
    [request_date] datetime not null,
    [response_date] datetime null,
    [request] nvarchar(max) not null,
    [response] nvarchar(max) null,
    constraint [PK_Messages] primary key ([Id])
);

create index [IX_Messages_RequestTransactionReferenceNumber]
    on [messages].[message_tracker] ([request_transaction_reference_number]);
