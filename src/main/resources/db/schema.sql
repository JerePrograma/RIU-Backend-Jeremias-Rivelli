create table searches (
                          search_id varchar2(64) primary key,
                          hotel_id varchar2(100) not null,
                          check_in date not null,
                          check_out date not null,
                          ages_csv varchar2(200) not null,
                          fingerprint varchar2(64) not null,
                          created_at timestamp not null
);

create index ix_searches_fingerprint on searches (fingerprint);